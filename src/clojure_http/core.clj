(ns clojure-http.core
  (:use clojure-http.request.parse
        clojure-http.method)
  (:use [clojure.contrib.io :only [reader writer]]
        [clojure.contrib.server-socket :only [create-server]])
  (:require clojure-http.method.default
            clojure-http.method.get
            clojure-http.method.head
            clojure-http.method.post
            clojure-http.method.put))

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
