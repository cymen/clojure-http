(ns clojure-http.method.post.default
  (:use clojure-http.method.post.post))

(defmethod post :default [request-headers body]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 501
      :Status-Message "Not implemented"
    }
    :Body "Not implemented"))
