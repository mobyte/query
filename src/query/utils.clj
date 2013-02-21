(ns query.utils)

(defn writeln [writer & text]
  (.write writer (apply str text))
  (.write writer "\n"))

(defn write [writer & text]
  (.write writer (apply str text)))
