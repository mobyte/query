(defproject query "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.0-RC16"]
                 [org.clojure/tools.nrepl "0.2.1"]]
  :profiles {:dev {:dependencies [[org.clojure/java.jdbc "0.2.3"]
                                  [com.mysql/jdbc "5.1.5"]]}})
