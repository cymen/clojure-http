(ns clojure-http.request.parse-spec
  (:import (java.io ByteArrayInputStream))
  (:use speclj.core
        clojure-http.request.parse)
  (:use [clojure.contrib.io :only [reader]]))

(describe "parse"

  (it "reads from a stream until blank line"
    (let [content   "ABC123"
          string    (str content "\n\n")
          as_bytes  (. string getBytes)
          stream    (reader (ByteArrayInputStream. as_bytes))]
      (should= (list "ABC123") (readline-until-blank stream))))

  (it "reads a byte sequence"
    (let [as_bytes  (. "ABC123" getBytes)
          stream    (reader (ByteArrayInputStream. as_bytes))]
      (should= (seq as_bytes) (byte-seq stream))))

  (it "reads a character sequence"
    (let [content   "ABC123"
          as_bytes  (. content getBytes)
          stream    (reader (ByteArrayInputStream. as_bytes))]
      (should= (seq content) (char-seq stream))))

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
