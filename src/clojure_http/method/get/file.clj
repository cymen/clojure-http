(ns clojure-http.method.get.file
  (:import (java.io File))
  (:use clojure-http.method.get.filesystem
        clojure-http.utility.datetime
        clojure-http.response)
  (:use [clojure.contrib.io :only [copy input-stream reader]])
  (:use [pantomime.mime :only [mime-type-of]]))

(defmethod filesystem :file [request-headers file filename out]
  (do
    ; TODO check for If-Modified-Since and return a 304 (Not Modified) if appropriate
    (with-open [*in* (reader filename)]
      (println (:HTTP-Version request-headers) "200 OK")
      (println "Content-Type:" (mime-type-of filename))
      (println "Content-Length:" (-> filename File. .length))
      (println "Connection: close")
      (println "Date:" (datetime-in-gmt))
      (println "Last-Modified:" (datetime-in-gmt (.lastModified file)))
      (println "Accept-Ranges: none")
      (println "Server: clip-clop/0.1")
      (println "")
      (copy (input-stream filename) out))))
