(ns clojure-http.method.head
  (:use [clojure-http.method]))

(defmethod method :HEAD [request-headers]
  (println "HEAD!"))
