(ns query.query-test
  (:require [clojure.test :refer :all]
            [query.query :refer :all]
            [clojure.pprint :refer [pprint]]
            [clojure.java.jdbc :as jdbc]))

(def ^:private db {:db "query_example"
                   :password ""
                   :classname "org.postgresql.Driver"
                   :subprotocol "postgresql"
                   :user "postgres"
                   :subname "//localhost:5432/query_example"})

(defn test-select [& client-ids]
  (select db :clients
          (fields :clients.name
                  (as :emails.value :email)
                  (as :phones.value :phone))
          (inner-join [:phones (on (:= :phones.client :clients.id))]
                      [:emails (on (:= :emails.client :clients.id))])
          (where (:and (:in :clients.id (or client-ids [1]))
                       (:not (:= :emails.value "spammer@hacker.org"))))
          (order [:clients.id :desc])
          (limit 20)))

(comment defn test-select-group []
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
