(ns db.conf)

(def db-variant {:direct {:classname "com.mysql.jdbc.Driver"
                          :subprotocol "mysql"
                          :subname (str "//localhost:3306/query_example")
                          :user "root"
                          :password "root"}
                 :connpool {:name "qeury_db"}})

(def db (db-variant :direct))
