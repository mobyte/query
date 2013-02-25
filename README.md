## Usage

```clj
(with-db db
  (select table-clients
          (fields clients-name
                  (as emails-value :email)
                  (as phones-value :phone))
          (inner-join [table-phones (on (:= phones-client clients-id))]
                      [table-emails (on (:= emails-client clients-id))])
          (where (:and (:in clients-id [2 4])
                       (:not (:= emails-value "spammer@hacker.org"))))
          (order [clients-id :desc])
          (limit 20)))

=> ({:phone "+2222222", :email "client2@somewere.com", :name "client2"}
    {:phone "+2221111", :email "client2@somewere.com", :name "client2"}
    {:phone "+2220000", :email "client2@somewere.com", :name "client2"})
```

```clj
(with-db db (select table-clients
                    (fields clients-id
                            clients-name 
                            (as (sqlfn :count clients-id) 
                                :emails_num))
                    (inner-join [table-emails (on (:= emails-client clients-id))])
                    (group clients-id)
                    (where (:not (:= emails-value "spammer@hacker.org")))
                    (having (:> (sqlfn :count clients-id) 1))))

=> ({:emails_num 2, :name "user1", :id 1}
    {:emails_num 3, :name "guest3", :id 3})
```

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
