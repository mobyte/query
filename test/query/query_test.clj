(ns query.query-test
  (:use clojure.test
        query.query
        query.gentablecolumns
        db.entities
        [clojure.pprint :only (pprint)])
  (:require [db.conf]))

(def ^:private db db.conf/db)

(defn test-select [& client-ids]
  (with-db db
    (select table-clients
            (fields clients-name
                    (as emails-value :email)
                    (as phones-value :phone))
            (inner-join [table-phones (on (:= phones-client clients-id))]
                        [table-emails (on (:= emails-client clients-id))])
            (where (:and (:in clients-id (or client-ids [1]))
                         (:not (:= emails-value "spammer@hacker.org"))))
            (order [clients-id :desc])
            (limit 20))))

(defn test-select-group []
  (with-db db
    (select table-clients
            (fields clients-id
                    clients-name 
                    (as (sqlfn :count clients-id) 
                        :emails_num))
            (inner-join [table-emails (on (:= emails-client clients-id))])
            (group clients-id)
            (where (:not (:= emails-value "spammer@hacker.org")))
            (having (:> (sqlfn :count clients-id) 1)))))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
