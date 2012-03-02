(ns clojure-http.get-file
  (:import (java.io File))
  (:use [clojure-http.filesystem])
  (:use [clojure-http.datetime])
  (:use [clojure-http.response])
  (:use [clojure.contrib.io :only [copy input-stream reader]])
  (:use [pantomime.mime :only [mime-type-of]]))


(defmethod filesystem :file [request-headers file out]
  (let [filename (.getName file)]
    (do
      (print (headers {:Content-Type "text/html"})))))
