(ns clojure-http.method.get.default
  (:use clojure-http.method.get.filesystem
        clojure-http.utility.datetime
        clojure-http.response)
  (:use [clojure.contrib.io :only [copy input-stream reader writer]]))

(defmethod filesystem :default [request-headers file filename]
  (hash-map
    :Status-Line {
      :HTTP-Version (:HTTP-Version request-headers)
      :Status-Code 404
      :Status-Message "Not Found"
    }
    :Body "File not found"))
