(ns clojure-http.core
  (:use [clojure-http.parse-request])
  (:use [clojure-http.method])
  (:use [clojure-http.method-get])
  (:use [clojure-http.method-head])
  (:use [clojure.contrib.io :only [reader writer]])
  (:use [clojure.contrib.server-socket  :only [create-server]])
  (:use [clj-time.core                  :only [now]])
  (:use [clj-time.coerce                :only [from-long]])
  (:use [clj-time.format                :only [formatter unparse]]))

(defmethod method "HEAD" [request-headers *out*]
  (println "HEAD!"))

(defmethod method "POST" [request-headers *out*]
  (do
    (println (:HTTP-Version request-headers) "200 OK")
    (println "Connection: close")
    (println "Date:" (unparse custom-formatter (now)))
    (println "Accept-Ranges: none")
    (println "Server: clip-clop/0.1")
    (println "")
    (println (parse-request-body request-headers))))

(defmethod method "PUT" [request-headers *out*]
  (do
    (println (:HTTP-Version request-headers) "200 OK")
    (println "Connection: close")
    (println "Date:" (unparse custom-formatter (now)))
    (println "Accept-Ranges: none")
    (println "Server: clip-clop/0.1")
    (println "")
    (println (parse-request-body request-headers))))

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
