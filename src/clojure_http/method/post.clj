(ns clojure-http.method.post
  (:use clojure-http.method
        clojure-http.method.post.parse-body
        clojure-http.method.post.default
        clojure-http.method.post.urlencoded
        clojure-http.request.parse))

(defmethod method :POST [request-headers]
  (let [length (Integer/parseInt (:Content-Length request-headers))
        body (fn [] (byte-seq *in* length))]
    (hash-map
      :Status-Line {
        :HTTP-Version (:HTTP-Version request-headers)
        :Status-Code 200
        :Status-Message "OK"
      }
      :Body (parse-body request-headers body))))
