(ns clojure-http.method.post
  (:use clojure-http.method
        clojure-http.request.parse))

(defmethod method :POST [request-headers]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 200
      :Status-Message "OK"
    }
    :Body (parse-request-body request-headers)))
