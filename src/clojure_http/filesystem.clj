(ns clojure-http.filesystem)

(defmulti filesystem
  (fn [request-headers file out]
    (if (.exists file)
      (cond
        (.isFile file) :file
        (.isDirectory file) :directory))))
