(ns clojure-http.method)

(defmulti method
  (fn [request-headers out] (:Method request-headers)))
