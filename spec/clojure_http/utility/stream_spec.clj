(ns clojure-http.utility.stream-spec
  (:import (java.io ByteArrayInputStream))
  (:use speclj.core
        clojure-http.utility.stream)
  (:use [clojure.contrib.io :only [reader]]))

(describe "stream"

  (it "reads from a stream until blank line"
    (let [content   "ABC123"
          string    (str content "\n\n")
          as_bytes  (. string getBytes)
          stream    (reader (ByteArrayInputStream. as_bytes))]
      (should= (list "ABC123") (readline-until-emptyline stream))))

)

(run-specs)
