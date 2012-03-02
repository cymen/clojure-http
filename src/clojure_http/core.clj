(ns clojure-http.core
  (:use clojure-http.parse-request
        clojure-http.method)
  (:use [clojure.contrib.io :only [reader writer]]
        [clojure.contrib.server-socket :only [create-server]])
  (:require clojure-http.method-get
            clojure-http.method-head
            clojure-http.method-post
            clojure-http.method-put
            clojure-http.method-unknown))

(defn http-server [port]
  (letfn [(http [in out]
    (binding [*in* (reader in)
              *out* (writer out)]
      (do
        (method (parse-request-headers *in*) out)
        (. *in* close)
        (. *out* close))))]
    (create-server port http)))

(defn -main []
  (http-server 5000))
