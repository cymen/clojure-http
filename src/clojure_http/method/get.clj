(ns clojure-http.method.get
  (:import (java.io File))
  (:use clojure-http.method
        clojure-http.method.get.filesystem
        clojure-http.utility.datetime)
  (:require clojure-http.method.get.default
            clojure-http.method.get.directory
            clojure-http.method.get.file))

; TODO how can I have this set in core or in project.clj and get the value from there?
(def root "public")

(defn resolve-file [request-headers]
  (str root (:Request-URI request-headers)))

(defmethod method :GET [request-headers]
  (let [filename (resolve-file request-headers)]
    (let [file (-> filename File.)]
      (filesystem request-headers file filename))))
