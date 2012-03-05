(ns clojure-http.method)

(defmulti method
  (fn [request-headers] (keyword (:Method request-headers))))
