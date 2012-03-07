(ns clojure-http.method.put
  (:use clojure-http.method
        clojure-http.method.put.put
        clojure-http.method.put.default
        clojure-http.method.put.urlencoded
        clojure-http.request.parse))

(defmethod method :PUT [request-headers in]
  (let [length  (Integer/parseInt (:Content-Length request-headers))
        body    (fn [] (byte-seq in length))]
    (put request-headers body)))
