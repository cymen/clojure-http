(ns clojure-http.core
  (:use [clojure-http.parse-request])
  (:use [clojure-http.methods])
  (:use [clojure-http.method])
  (:use [clojure.contrib.io :only [reader writer]])
  (:use [clojure.contrib.server-socket  :only [create-server]]))

(defn http-server [port]
  (letfn [(http [in out]
    (binding [*in* (reader in)
              *out* (writer out)]
      (let [request-headers
        (parse-request-headers *in*)]
        (method request-headers out)
        (flush)
      )
    ))]
    (create-server port http)))

(defn -main []
  (http-server 5000))
