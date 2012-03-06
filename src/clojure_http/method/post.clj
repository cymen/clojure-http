(ns clojure-http.method.post
  (:use clojure-http.method
        clojure-http.method.post.parse-body
        clojure-http.method.post.default
        clojure-http.method.post.urlencoded))

(defmethod method :POST [request-headers]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 200
      :Status-Message "OK"
    }
    :Body (parse-body request-headers)))
