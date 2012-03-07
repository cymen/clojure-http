(ns clojure-http.method.post.urlencoded
  (:import (java.net URLDecoder))
  (:use clojure-http.method.post.post
        clojure-http.request.parse))

(defn decode-body [body]
  (URLDecoder/decode
    (apply str
      (map char (body)))))

(defmethod post :application/x-www-form-urlencoded [request-headers body]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 200
      :Status-Message "OK"
    }
    :Body (decode-body body)))
