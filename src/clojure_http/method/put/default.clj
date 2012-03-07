(ns clojure-http.method.put.default
  (:use clojure-http.method.put.put))

(defmethod put :default [request-headers body]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 501
      :Status-Message "Not implemented"
    }
    :Body "Not implemented"))
