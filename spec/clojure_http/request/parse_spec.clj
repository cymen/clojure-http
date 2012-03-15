(ns clojure-http.request.parse-spec
  (:import (java.io ByteArrayInputStream))
  (:use speclj.core
        clojure-http.request.parse)
  (:use [clojure.contrib.io :only [reader]]))

(describe "parse"

  (it "parses 'key: value' line"
    (let [k     "Content-Length"
          v     "592"
          line  (str k ": " v)]
      (should= (list k v) (parse-key-value line))))

  (it "parses 'key: value' line into collection as (keyword k) value"
    (let [k     "User-Agent"
          v     "SomeBrowser v1.0"
          line  (str k ": " v)
          kw    (keyword k)]
      (should= (hash-map kw v) (parse-key-value-into {} line))))

  (it "parses a list of 'key: value' lines into collection as (keyword k) value"
    (let [k       "User-Agent"
          v       "SomeBrowser v1.0"
          line    (str k ": " v)
          lines   (list line)
          k2      "MyKey"
          v2      "MyValue"
          lines   (conj lines (str k2 ": " v2))
          kw      (keyword k)
          kw2     (keyword k2)
          result  (parse-request-headers lines)]
      (should= v (kw result))
      (should= v2 (kw2 result))))

  (it "parses an HTTP request line like 'GET / HTTP/1.1'"
    (let [method  "GET"
          uri     "/abc"
          version "HTTP/1.1"
          line    (str method " " uri " " version)
          request (parse-request-line line)]
      (should= "GET" (:Method request))
      (should= "/abc" (:Request-URI request))
      (should= "HTTP/1.1" (:HTTP-Version request))))


  (it "parses HTTP header requests from a stream"
      (let [k         "User-Agent"
            v         "SomeBrowser v1.0"
            line      (str k ": " v)
            kw        (keyword k)
            method    "GET"
            uri       "/"
            version   "HTTP/5.3"
            request   (str method " " uri " " version "\n" line "\n\n")
            as_bytes  (. request getBytes)
            stream    (reader (ByteArrayInputStream. as_bytes))
            parsed    (parse-request stream)]
        (should= method (:Method parsed))
        (should= v ((keyword k) parsed))))

)

(run-specs)
