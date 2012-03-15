(ns clojure-http.method.put-spec
  (:import (java.net URLEncoder)
           (java.io ByteArrayInputStream))
  (:use speclj.core
        clojure-http.method
        clojure-http.method.put)
  (:use [clojure.contrib.io :only [reader]]))

(describe "put"

  (it "unparses a urlencoded body"
    (let [value       "Field1=Value1&Field2=Value2"
          body        (URLEncoder/encode value)
          stream      (reader (ByteArrayInputStream. (. body getBytes)))
          request     (hash-map
                        :Method "PUT"
                        :Request-URI "/anywhere"
                        :Content-Type "application/x-www-form-urlencoded"
                        :Content-Length (str (count body)))
          response    (method request stream)
          body        (:Body response)]
      (should= value body)))

)

(run-specs)
