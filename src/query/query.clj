(ns query.query
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.string :as str])
  (:import (java.sql PreparedStatement)))

(def ^:dynamic *debug-mode* nil)

;;;; utils

(defn- psymbol [s]
  [s []])

(defn- field-name [field]
  (cond (map? field) (str (:table field) "." (:name field))
        (keyword? field) (name field)
        :else field))

(defn- pe-field-name [field]
  (cond (map? field) (psymbol (str (:table field) "." (:name field)))
        (keyword? field) (psymbol (name field))
        (sequential? field) field
        :else (psymbol (name field))))

(defn- join-with
  "[\"s1\" \"s2\" ... \"sn\"], \"sep\" -> \"s1 sep s2 sep ... sep sn\""
  [items sep]
  (apply str (str/join (str sep " ") items)))

(defn- cjoin
  "Join strings with comma separator"
  [& items]
  (join-with items ", "))

(defn- sjoin
  "Join strings with space separator"
  [& items]
  (join-with items ""))

;;;; pexpression utils ([<string expression>" [args]])

(defn- wrap-brackets
  "[\"expr\" args] -> [\"(expr)\" args]"
  [[sexpr args]]
  [(sjoin "(" sexpr ")") args])

(defn- add-front
  "\"str\", [\"expr\" args] -> [\"str expr\" args]"
  [s [sexpr args]]
  [(sjoin s sexpr) args])

(defn- to-front
  "\"str\", [\"expr\" args] -> [\"str expr\" args]"
  [s [sexpr args]]
  [(str s sexpr) args])

(defn- pexpr
  "expr -> [\"sexpr\" args]"
  [expr]
  (cond (sequential? expr) (wrap-brackets expr)
        (map? expr) [(field-name expr) []]
        :else ["?" [expr]]))

(defn- psymbol [s]
  "\"s\" -> [\"s\" []]"
  [s []])

(defn- join-pexprs
  "[[\"expr1\" [arg11 arg12 ...]] [\"expr2\" [arg21 arg22 ...]] ...]\n-> [\"expr1 expr2 ...\" [arg11 arg12 ... arg21 arg22 ...]]"
  [& pexprs]
  (let [[sexprs args] (map #(map % pexprs) 
                           [first second])]
    [(apply sjoin sexprs) (apply concat args)]))

(defn- pe-join [sep exprs]
  (apply join-pexprs (interpose (psymbol sep) (map pexpr exprs))))

(defn- pe-sjoin
  "[expr1 expr2 ...] -> [\"sexpr2 sexpr2 ...\" [arg1 arg2 ...]]"
  [exprs]
  (pe-join "" exprs))

(defn- pe-cjoin [exprs]
  "[expr1 expr2 ...] -> [\"sexpr2, sexpr2, ...\" [arg1 arg2 ...]]"
  (pe-join "," exprs))

;;;; process conditions

(defn e-in [field exprs]
  (join-pexprs (pexpr field)
               (psymbol "in")
               (wrap-brackets (pe-cjoin exprs))))

(defn e-not [sexpr]
  (add-front "not" (wrap-brackets sexpr)))

(defn e-op [op & exprs]
  (pe-join (name op) exprs))

(def ^:private ops #{:in :not :and :or := :> :>= :< :<=})
(def ^:private spec-ops #{:in :not})

(defn prepare-expr [expr]
  (if (and (list? expr)
           (ops (first expr)))
    (let [op (first expr)
          args (rest expr)]
      (let [new-args (map prepare-expr args)]
        (if (spec-ops op)
          `(~(symbol (str "e-" (name op))) ~@new-args)
          `(~'e-op ~(keyword op) ~@new-args))))
    expr))

;;;; where

(defn where* [pexpr]
  [:where (add-front "where" pexpr)])

(defmacro where [expr]
  `(where* ~(prepare-expr expr)))

;;;; limit

(defn limit [n]
  [:limit (join-pexprs (psymbol "limit") (pexpr n))])

;;;; order

(defn order [& fields]
  [:order (psymbol (sjoin "order by" 
                          (apply cjoin 
                                 (map #(if (vector? %)
                                         (sjoin (field-name (first %))
                                                (name (second %)))
                                         (field-name %))
                                      fields))))])

;;;; group

(defn group [& fields]
  [:group (psymbol (sjoin "group by"
                          (apply cjoin (map field-name fields))))])

;;;; having

(defn having* [pexpr]
  [:having (add-front "having" pexpr)])

(defmacro having [expr]
  `(having* ~(prepare-expr expr)))

;;;; join

(defn on* [pexpr]
  (add-front "on" pexpr))

(defmacro on [expr]
  `(on* ~(prepare-expr expr)))

(defn- join* [type joins]
  [:join (apply join-pexprs 
                (map (fn [[table pcond]]
                       (apply join-pexprs (concat (map psymbol [(name type) 
                                                                "join" 
                                                                table])
                                                  [pcond])))
                     joins))])

(defn inner-join [& joins]
  (join* :inner joins))

(defn outer-join [& joins]
  (join* :outer joins))

;;;; fields

(defn sqlfn [f & exprs]
  (to-front (name f) (wrap-brackets (pe-cjoin exprs))))

(defn as [expr alias]
  (join-pexprs (pe-field-name expr)
               (psymbol "as") 
               (psymbol (name alias))))

(defn fields [& flds]
  [:fields (if (#{:all :* [:all] [:*]} flds)
             (psymbol "*")
             (apply join-pexprs (interpose (psymbol ",") (map pe-field-name flds))))])

;;;; select

(def ^:private form-order [:select
                           :fields
                           :from
                           :join
                           :where
                           :order
                           :group
                           :having
                           :limit])

(defn- sorted-pexprs [forms]
  (reduce (fn [res elem] (if (forms elem)
                           (conj res (forms elem))
                           res))
          []
          form-order))

(defn- add-select [forms]
  (conj forms [:select (psymbol "select")]))

(defn- add-from [forms table]
  (conj forms [:from (psymbol (sjoin "from" table))]))

(defn- add-optional-fields [forms]
  (if (:fields forms)
    forms
    (conj forms (fields :*))))

(defn select [table & forms]
  (let [forms (into {} (-> forms
                           (add-select)
                           (add-from table)
                           (add-optional-fields)))
        pexprs (sorted-pexprs forms)
        pquery (apply join-pexprs pexprs)
        query (into [] (cons (first pquery) (second pquery)))]
    (when *debug-mode* (println pquery) (println query))
    (jdbc/with-query-results rs query 
      (doall rs))))

;;;; with db

(defmacro with-db [db & body]
  `(jdbc/with-connection ~db
     ~@body))

(defmacro with-tr [db & body]
  `(jdbc/with-connection ~db
     (jdbc/transaction
      (try ~@body
           (catch Exception e#
             (try (jdbc/set-rollback-only)
                  (catch Exception inner-e#
                    (throw inner-e#)))
             (throw e#))))))
