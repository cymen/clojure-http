(ns clojure-http.method.default
  (:use clojure-http.method
        clojure-http.utility.datetime))

(defmethod method :default [request-headers]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 501
      :Status-Message "Not implemented"
    }
    :Body "Not implemented"))
