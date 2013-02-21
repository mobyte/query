(clojure.core/ns db.entities)

;;;; clients
(def table-clients "clients")
(def clients-id {:type {:size 10, :name "INT UNSIGNED"}, :table "clients", :name "id"})
(def clients-name {:type {:size 255, :name "VARCHAR"}, :table "clients", :name "name"})

;;;; emails
(def table-emails "emails")
(def emails-id {:type {:size 10, :name "INT UNSIGNED"}, :table "emails", :name "id"})
(def emails-client {:type {:size 10, :name "INT"}, :table "emails", :name "client"})
(def emails-value {:type {:size 255, :name "VARCHAR"}, :table "emails", :name "value"})

;;;; phones
(def table-phones "phones")
(def phones-id {:type {:size 10, :name "INT UNSIGNED"}, :table "phones", :name "id"})
(def phones-client {:type {:size 10, :name "INT"}, :table "phones", :name "client"})
(def phones-value {:type {:size 64, :name "VARCHAR"}, :table "phones", :name "value"})

