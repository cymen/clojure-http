(ns clojure-http.method.post.parse-body)

(defmulti parse-body (fn [request-headers] (keyword (:Content-Type request-headers))))
