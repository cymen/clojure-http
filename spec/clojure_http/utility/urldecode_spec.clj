(ns clojure-http.utility.urldecode-spec
  (:import (java.net URLEncoder)
           (java.io ByteArrayInputStream))
  (:use speclj.core
        clojure-http.utility.urldecode
        clojure-http.utility.sequences)
  (:use [clojure.contrib.io :only [reader]]))

(describe "urldecode"

  (it "unencodes a urlencoded string"
    (let [string  "Abc=123&Zyx=555"
          encoded (URLEncoder/encode string)
          stream  (reader (ByteArrayInputStream. (. encoded getBytes)))]
      (should= string (decode (byte-seq stream (. encoded length))))))

)

(run-specs)
