(ns clojure-http.method.head-spec
  (:import (java.io ByteArrayOutputStream)
           (java.util Arrays))
  (:use speclj.core
        clojure-http.method
        clojure-http.method.head)
  (:use [clojure-http.config :as config]
        [clojure.contrib.io :only [copy input-stream writer]]))

(describe "head"

  (it "headers for HEAD same as headers for GET on '/'"
    (let [get-response  (method { :Method "GET" :Request-URI "/" } *in*)
          head-response (method { :Method "HEAD" :Request-URI "/" } *in*)]
      (should= (:Status-Line get-response) (:Status-Line head-response))
      (should= (:Headers get-response) (:Headers head-response))))

  (it "headers for HEAD on a file same as headers for GET"
    (let [filename      "/test.txt"
          get-response  (method { :Method "GET" :Request-URI filename } *in*)
          head-response (method { :Method "HEAD" :Request-URI filename } *in*)]
      (should= (:Status-Line get-response) (:Status-Line head-response))
      (should= (:Headers get-response) (:Headers head-response))))

  (it "headers for HEAD on missing file same as GET"
    (let [filename "/file-does-not-exist.txt"
          get-response  (method { :Method "GET" :Request-URI filename } *in*)
          head-response (method { :Method "HEAD" :Request-URI filename } *in*)]
      (should= (:Status-Line get-response) (:Status-Line head-response))
      (should= (:Headers get-response) (:Headers head-response))))

)

(run-specs)
