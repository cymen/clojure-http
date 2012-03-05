(ns clojure-http.method.post
  (:use clojure-http.method
        clojure-http.request.parse
        clojure-http.utility.datetime))

(defmethod method :POST [request-headers]
  (do
    (println (:HTTP-Version request-headers) "200 OK")
    (println "Connection: close")
    (println "Date:" (datetime-in-gmt))
    (println "Accept-Ranges: none")
    (println "Server: clip-clop/0.1")
    (println "")
    (println (parse-request-body request-headers))))
