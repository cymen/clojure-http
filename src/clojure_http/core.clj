(ns clojure-http.core
  (:use clojure-http.parse-request
        clojure-http.methods
        clojure-http.method)
  (:use [clojure.contrib.io :only [reader writer]]
        [clojure.contrib.server-socket :only [create-server]]))

(defn http-server [port]
  (letfn [(http [in out]
    (binding [*in* (reader in)
              *out* (writer out)]
      (do
        (method (parse-request-headers *in*) out)
        (flush))))]
    (create-server port http)))

(defn -main []
  (http-server 5000))
