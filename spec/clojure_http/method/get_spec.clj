(ns clojure-http.method.get-spec
  (:import (java.io ByteArrayOutputStream)
           (java.io File)
           (java.util Arrays))
  (:use speclj.core
        clojure-http.method
        clojure-http.method.get
        clojure-http.utility.datetime)
  (:use [clojure-http.config :as config]
        [clojure.contrib.io :only [copy input-stream writer]]))

(defn- run-fn-to-output-stream [function]
  (let [stream  (ByteArrayOutputStream.)
        _       (function stream)]
    stream))

(defn- read-file-to-output-stream [filename]
  (let [stream  (ByteArrayOutputStream.)
        _       (copy (input-stream (str config/root filename)) stream)]
    stream))

(defn- compare-output-streams [stream1 stream2]
  (. Arrays equals (.toByteArray stream1) (.toByteArray stream2)))

(describe "get"

  (it "resolves a request URI to a file within root"
    (should= (str config/root "/nachos") (resolve-file {:Request-URI "/nachos"})))

  (it "responds to a request for the root"
    (should= 200 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI "/" } *in*)))))

  (it "responds to a request for a file"
    (let [filename "/test.txt"]
      (should= 200 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI filename } *in*))))))

  (it "responds to a request for a not-present file"
    (let [filename "/file-does-not-exist.txt"]
      (should= 404 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI filename } *in*))))))

  (it "checks the If-Modified-Since header and returns a 304"
    (let [filename  "/test.txt"
          file      (-> (str config/root filename) File.)
          modified  (. file lastModified)
          date      (unparse-http-datetime modified)
          response  (method { :Method "GET" :Request-URI filename :If-Modified-Since date } *in*)]
      (should= 304 (:Status-Code (:Status-Line response)))))

  (it "checks the If-Modified-Since header and returns a 200 if modified since"
    (let [filename  "/test.txt"
          file      (-> (str config/root filename) File.)
          modified  (- (. file lastModified) 1000)
          date      (unparse-http-datetime modified)
          response  (method { :Method "GET" :Request-URI filename :If-Modified-Since date } *in*)]
      (should= 200 (:Status-Code (:Status-Line response)))))

  (it "transfers text file"
    (let [filename      "/test.txt"
          response      (method { :Method "GET" :Request-URI filename } *in*)
          body          (:Body response)
          http-result   (run-fn-to-output-stream body)
          actual-result (read-file-to-output-stream filename)]
      (should= true (compare-output-streams actual-result http-result))))

  (it "transfers binary file"
    (let [filename      "/clojure-icon.gif"
          response      (method { :Method "GET" :Request-URI filename } *in*)
          body          (:Body response)
          http-result   (run-fn-to-output-stream body)
          actual-result (read-file-to-output-stream filename)]
      (should= true (compare-output-streams actual-result http-result))))

)

(run-specs)
