(ns clojure-http.method.get.file
  (:import (java.io File))
  (:use clojure-http.method.get.filesystem
        clojure-http.response
        clojure-http.utility.datetime)
  (:use [clojure.contrib.io :only [copy input-stream writer]])
  (:use [clj-time.core :only [after?]])
  (:use [pantomime.mime :only [mime-type-of]]))

(defn- modified-since? [since actual]
  (if (nil? since)
    true
    (after? actual (parse-http-datetime since))))

(defmethod filesystem :File [request-headers file filename]
    (let [last-modified (.lastModified file)]
      (if (not (modified-since? (:If-Modified-Since request-headers) (datetime-from-long last-modified)))
        (hash-map
          :Status-Line {
            :HTTP-Version (:HTTP-Version request-headers)
            :Status-Code 304
            :Status-Message "Not Modified"
          }
          :Headers {
            :Connection "close"
            :Server "clip-clop/0.1" })
        (hash-map
          :Status-Line {
            :HTTP-Version (:HTTP-Version request-headers)
            :Status-Code 200
            :Status-Message "OK"
          }
          :Headers {
            :Content-Type (mime-type-of filename)
            :Content-Length (-> filename File. .length)
            :Connection "close"
            :Date (datetime-in-gmt)
            :Last-Modified (datetime-in-gmt last-modified)
            :Accept-Ranges "none"
            :Server "clip-clop/0.1" }
          :Body (fn [output] (copy (input-stream filename) output))))))
