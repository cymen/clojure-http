(ns clojure-http.method-post
  (:use [clojure-http.method])
  (:use [clojure-http.parse-request])
  (:use [clojure-http.response]))

;(def custom-formatter (formatter "EEE, dd MMM yyyy HH:mm:ss 'GMT'"))

(defmethod method "POST" [request-headers *out*]
  (do
    (println (:HTTP-Version request-headers) "200 OK")
    (println "Connection: close")
    (println "Date:" (now-in-gmt)))
    (println "Accept-Ranges: none")
    (println "Server: clip-clop/0.1")
    (println "")
    (println (parse-request-body request-headers)))
