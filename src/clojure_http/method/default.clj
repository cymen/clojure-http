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
    :Headers {
      :Date (datetime-in-gmt)
      :Server "clip-clop/0.1"
      :Connection "close"
    }
    :Body "Not implemented"))
