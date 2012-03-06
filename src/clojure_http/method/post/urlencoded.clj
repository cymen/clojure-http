(ns clojure-http.method.post.urlencoded
  (:import (java.net URLDecoder))
  (:use clojure-http.method.post.parse-body
        clojure-http.request.parse))

(defmethod parse-body :application/x-www-form-urlencoded [request-headers]
  (if (contains? request-headers :Content-Length)
    (URLDecoder/decode
      (apply str
        (take (#(Integer/parseInt (:Content-Length request-headers)))
          (char-seq *in*))))))
