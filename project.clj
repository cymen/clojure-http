(defproject clojure-http "1.0.0-SNAPSHOT"
  :description "A basic HTTP server named clip-clop"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [com.novemberain/pantomime "1.1.0"]
                 [clj-time "0.3.7"]]
  :dev-dependencies [[speclj "2.1.1"]]
  :test-path "spec/"
  :main clojure-http.core)
