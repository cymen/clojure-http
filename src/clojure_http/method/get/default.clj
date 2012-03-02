(ns clojure-http.method.get.default
  (:use clojure-http.method.get.filesystem
        clojure-http.utility.datetime
        clojure-http.response))

(defmethod filesystem :default [request-headers file filename out]
  (do
    (println (:HTTP-Version request-headers) "404 Not Found")
    (println "Date:" (datetime-in-gmt))
    (println "Server: clip-clop/0.1")
    (println "Connection: close")
    (println "")
    (println "File not found")))
