(ns clojure-http.method.default
  (:use clojure-http.method
        clojure-http.utility.datetime))

(defmethod method :default [request-headers *out*]
  (println "500 or whatever HTTP code we can use for unimplemented/unknown method"))
