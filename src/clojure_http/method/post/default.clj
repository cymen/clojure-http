(ns clojure-http.method.post.default
  (:use clojure-http.method.post.parse-body))

(defmethod parse-body :default [request-headers body]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 501
      :Status-Message "Not implemented"
    }
    :Body "Not implemented"))
