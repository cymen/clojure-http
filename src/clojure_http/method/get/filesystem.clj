(ns clojure-http.method.get.filesystem)

(defmulti filesystem
  (fn [request-headers file filename out]
    (if (.exists file)
      (cond
        (.isFile file) :File
        (.isDirectory file) :Directory))))
