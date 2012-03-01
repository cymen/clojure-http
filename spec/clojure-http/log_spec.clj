(ns clojure-http.log-spec
  (:import (java.io ByteArrayOutputStream))
  (:use speclj.core
        clojure-http.log)
  (use [clojure.contrib.io :only [writer]]))

(describe "log"
  (it "should contain what it is written to it"
    (let [stream (ByteArrayOutputStream.)]
      (do
        (log "test123" (writer stream))
        (should (= "test123\n" (.toString stream)))))))

(run-specs)
