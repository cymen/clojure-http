(ns clojure-http.method.get-spec
  (:import (java.io ByteArrayOutputStream))
  (:use speclj.core
        clojure-http.method
        clojure-http.method.get)
  (use [clojure.contrib.io :only [writer]]))

(describe "get"

  (it "resolves a request URI to a file within root"
    (should= "public/nachos" (resolve-file {:Request-URI "/nachos"})))

  (it "responds to a request for a file"
    (let [filename "/test.txt"]
      (should= 200 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI filename }))))))

  (it "responds to a request for a not-present file"
    (let [filename "/file-does-not-exist.txt"]
      (should= 404 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI filename }))))))

  (it "can transfer text contents"
    (let [filename  "/test.txt"
          stream    (ByteArrayOutputStream.)
          response  (method { :Method "GET" :Request-URI filename })
          body      (:Body response)
          _         (body stream)
          text      (.toString stream)]
      (should= "test\n" text)))

  (it "can transfer binary contents"
    (let [filename  "/clojure-icon.gif"
          stream    (ByteArrayOutputStream.)
          response  (method { :Method "GET" :Request-URI filename })
          body      (:Body response)
          _         (body stream)
          result    (.toString stream)]
      (should= "test\n" result)))
)

(run-specs)
