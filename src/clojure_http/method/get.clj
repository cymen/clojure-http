(ns clojure-http.method.get
  (:import (java.io File))
  (:use clojure-http.method
        clojure-http.method.get.filesystem)
  (:use [clojure-http.config :as config])
  (:require clojure-http.method.get.default
            clojure-http.method.get.directory
            clojure-http.method.get.file))

(defn resolve-file [request-headers]
  (str config/root (:Request-URI request-headers)))

(defmethod method :GET [request-headers in]
  (let [filename (resolve-file request-headers)]
    (let [file (-> filename File.)]
      (filesystem request-headers file filename))))
