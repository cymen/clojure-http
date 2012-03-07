(ns clojure-http.method.post-spec
  (:import (java.net URLEncoder)
           (java.io ByteArrayInputStream))
  (:use speclj.core
        clojure-http.method
        clojure-http.method.post)
  (:use [clojure.contrib.io :only [reader]]))

(describe "post"

  (it "unparses a urlencoded body"
    (let [value       "Field1=Value1&Field2=Value2"
          body        (URLEncoder/encode value)
          stream      (reader (ByteArrayInputStream. (.getBytes body)))
          request     (hash-map
                        :Method "POST"
                        :Request-URI "/anywhere"
                        :Content-Type "application/x-www-form-urlencoded"
                        :Content-Length (str (count body)))
          response    (method request stream)
          body        (:Body response)]
      (should= value body)))
)

(run-specs)
