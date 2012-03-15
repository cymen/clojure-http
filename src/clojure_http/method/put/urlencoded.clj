(ns clojure-http.method.put.urlencoded
  (:use clojure-http.method.put.put
        clojure-http.request.parse
        clojure-http.utility.urldecode))

(defmethod put :application/x-www-form-urlencoded [request-headers body]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 200
      :Status-Message "OK"
    }
    :Body (decode (body))))
