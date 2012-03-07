(ns clojure-http.method.post-spec
  (:import (java.net URLEncoder)
           (java.io ByteArrayInputStream)
           (java.io ByteArrayOutputStream))
  (:use speclj.core
        clojure-http.method
        clojure-http.method.post)
  (:use [clojure.contrib.io :only [reader]]))

(defn- run-fn-to-output-stream [function]
  (let [stream  (ByteArrayOutputStream.)
        _       (function stream)]
    stream))

(describe "post"

  (it "unparses a urlencoded body"
    (let [value       "Field1=Value1&Field2=Value2"
          body        (URLEncoder/encode value)
          stream      (reader (ByteArrayInputStream. (.getBytes body)))
          response    (method
                        (hash-map
                          :Method "POST"
                          :Request-URI "/anywhere"
                          :Content-Type "application/x-www-form-urlencoded"
                          :Content-Length (str (count body))
                        )
                        stream)
          body        (:Body response)]
      (should= value body)))
)

(run-specs)
