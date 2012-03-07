(ns clojure-http.method)

(defmulti method
  (fn [request-headers in] (keyword (:Method request-headers))))
