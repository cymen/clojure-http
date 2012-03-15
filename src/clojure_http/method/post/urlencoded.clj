(ns clojure-http.method.post.urlencoded
  (:use clojure-http.method.post.post
        clojure-http.request.parse
        clojure-http.utility.urldecode))

(defmethod post :application/x-www-form-urlencoded [request-headers body]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 200
      :Status-Message "OK"
    }
    :Body (decode (body))))
