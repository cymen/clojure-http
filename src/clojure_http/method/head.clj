(ns clojure-http.method.head
  (:use [clojure-http.method]))

(defn- override-method [request-headers]
  (merge
    request-headers
    { :Method "GET" }))

(defmethod method :HEAD [request-headers]
  (dissoc (method (override-method request-headers)) :Body))
