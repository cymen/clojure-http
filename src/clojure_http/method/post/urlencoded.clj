(ns clojure-http.method.post.urlencoded
  (:import (java.net URLDecoder))
  (:use clojure-http.method.post.parse-body
        clojure-http.request.parse))

(defmethod parse-body :application/x-www-form-urlencoded [request-headers body]
  (URLDecoder/decode
    (apply str
      (map char (body)))))
