(ns clojure-http.method.put.put)

(defmulti put
  (fn [request-headers body]
    (if (contains? request-headers :Content-Length)
      (keyword (:Content-Type request-headers)))))
