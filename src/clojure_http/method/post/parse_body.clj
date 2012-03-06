(ns clojure-http.method.post.parse-body)

(defmulti parse-body
  (fn [request-headers body]
    (if (contains? request-headers :Content-Length)
      (keyword (:Content-Type request-headers)))))
