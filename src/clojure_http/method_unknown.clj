(ns clojure-http.method-unknown
  (:use [clojure-http.method])
  (:use [clojure-http.parse-request])
  (:use [clojure-http.response]))

(defmethod method :default [request-headers *out*]
  (println "500 or whatever HTTP code we can use for unimplemented/unknown method"))
