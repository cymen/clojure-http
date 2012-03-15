(ns clojure-http.method-spec
  (:use speclj.core
        clojure-http.method)
  (:require clojure-http.method.default
            clojure-http.method.get
            clojure-http.method.head
            clojure-http.method.post
            clojure-http.method.put))

(describe "method"

  (it "responds to a METHOD_MISSING request with 501"
    (let [response (method { :Method "METHOD_MISSING" :Request-URI "/" } *in*)]
      (should= 501 (:Status-Code (:Status-Line response)))))

  (it "responds to a GET request with 200"
    (let [response (method { :Method "GET" :Request-URI "/" } *in*)]
      (should= 200 (:Status-Code (:Status-Line response)))))

  (it "responds to a HEAD request with 200"
    (let [response (method { :Method "HEAD" :Request-URI "/" } *in*)]
      (should= 200 (:Status-Code (:Status-Line response)))))

)

(run-specs)
