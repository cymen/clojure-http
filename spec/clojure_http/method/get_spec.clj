(ns clojure-http.method.get-spec
  (:import (java.io ByteArrayOutputStream)
           (java.util Arrays))
  (:use speclj.core
        clojure-http.method
        clojure-http.method.get)
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
    (should= 200 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI "/" })))))

  (it "responds to a request for a file"
    (let [filename "/test.txt"]
      (should= 200 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI filename }))))))

  (it "responds to a request for a not-present file"
    (let [filename "/file-does-not-exist.txt"]
      (should= 404 (:Status-Code (:Status-Line (method { :Method "GET" :Request-URI filename }))))))

  (it "transfers text file"
    (let [filename      "/test.txt"
          response      (method { :Method "GET" :Request-URI filename })
          body          (:Body response)
          http-result   (run-fn-to-output-stream body)
          actual-result (read-file-to-output-stream filename)]
      (should= true (compare-output-streams actual-result http-result))))

  (it "transfers binary file"
    (let [filename      "/clojure-icon.gif"
          response      (method { :Method "GET" :Request-URI filename })
          body          (:Body response)
          http-result   (run-fn-to-output-stream body)
          actual-result (read-file-to-output-stream filename)]
      (should= true (compare-output-streams actual-result http-result))))
)

(run-specs)
