(ns clojure-http.method-unknown
  (:use [clojure-http.method])
  (:use [clojure-http.parse-request])
  (:use [clojure-http.response]))

(defmethod method :default [request-headers *out*]
  (reply-with "500 or whatever HTTP code we can use for unimplemented/unknown method"))
