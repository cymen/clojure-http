(ns clojure-http.method.get
  (:import (java.io File))
  (:use clojure-http.method
        clojure-http.method.get.filesystem
        clojure-http.utility.datetime)
  (:use [clojure.contrib.io :only [copy input-stream reader]])
  (:require clojure-http.method.get.directory
            clojure-http.method.get.file))

; TODO how can I have this set in core or in project.clj and get the value from there?
(def root "public")

(defn resolve-file [request-headers]
  (str root (:Request-URI request-headers)))

(defmethod method "GET" [request-headers out]
  (let [filename (resolve-file request-headers)]
    (let [file (-> filename File.)]
      (if (.exists file)
        (filesystem request-headers file filename out)
        (do
          (println (:HTTP-Version request-headers) "404 Not Found")
          (println "Date:" (datetime-in-gmt))
          (println "Server: clip-clop/0.1")
          (println "Connection: close")
          (println "")
          (println "File not found"))))))
