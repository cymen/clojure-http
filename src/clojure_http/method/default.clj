(ns clojure-http.method.default
  (:use clojure-http.method
        clojure-http.utility.datetime))

(defmethod method :default [request-headers *out*]
  (println "501 Not implemented"))
