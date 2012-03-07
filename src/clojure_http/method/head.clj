(ns clojure-http.method.head
  (:use [clojure-http.method]))

(defn- override-method [request-headers]
  (merge
    request-headers
    { :Method "GET" }))

(defmethod method :HEAD [request-headers in]
  (dissoc (method (override-method request-headers) in) :Body))
