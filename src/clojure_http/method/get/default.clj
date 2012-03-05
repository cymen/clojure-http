(ns clojure-http.method.get.default
  (:use clojure-http.method.get.filesystem
        clojure-http.utility.datetime
        clojure-http.response))

(defmethod filesystem :default [request-headers file filename]
  (do
    (println (:HTTP-Version request-headers) "404 Not Found")
    (println unparse-headers
      { :Date (datetime-in-gmt)
        :Server "clip-clop/0.1"
        :Connection "close"})
    (println "File not found")))
