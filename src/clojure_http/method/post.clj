(ns clojure-http.method.post
  (:use clojure-http.method
        clojure-http.method.post.post
        clojure-http.method.post.default
        clojure-http.method.post.urlencoded
        clojure-http.utility.sequences))

(defmethod method :POST [request-headers in]
  (let [length  (Integer/parseInt (:Content-Length request-headers))
        body    (fn [] (byte-seq in length))]
    (post request-headers body)))
