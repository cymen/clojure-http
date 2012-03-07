(ns clojure-http.method.post.post)

(defmulti post
  (fn [request-headers body]
    (if (contains? request-headers :Content-Length)
      (keyword (:Content-Type request-headers)))))
