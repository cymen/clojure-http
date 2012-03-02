(ns clojure-http.method.get.file
  (:import (java.io File))
  (:use clojure-http.method.get.filesystem
        clojure-http.response
        clojure-http.utility.datetime)
  (:use [clojure.contrib.io :only [copy input-stream reader]])
  (:use [clj-time.core :only [after?]])
  (:use [pantomime.mime :only [mime-type-of]]))

(defn- modified-since? [since actual]
  (if (nil? since)
    true
    (after? actual (parse-http-datetime since))))

(defmethod filesystem :File [request-headers file filename out]
  (let [last-modified (.lastModified file)]
    (if (not (modified-since? (:If-Modified-Since request-headers) (datetime-from-long last-modified)))
      (do
        (println (:HTTP-Version request-headers) "304 Not Modified")
        (println "Connection: close")
        (println "Server: clip-clop/0.1"))
      (do
        (with-open [*in* (reader filename)]
          (println (:HTTP-Version request-headers) "200 OK")
          (println "Content-Type:" (mime-type-of filename))
          (println "Content-Length:" (-> filename File. .length))
          (println "Connection: close")
          (println "Date:" (datetime-in-gmt))
          (println "Last-Modified:" (datetime-in-gmt last-modified))
          (println "Accept-Ranges: none")
          (println "Server: clip-clop/0.1")
          (println "")
          (copy (input-stream filename) out))))))
