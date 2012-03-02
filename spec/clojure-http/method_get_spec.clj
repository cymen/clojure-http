(ns clojure-http.method.get-spec
  (:use speclj.core
        clojure-http.method.get))

(describe "method-get"
  (it "can resolve a request URI to a file within root"
    (should (= "public/nachos" (resolve-file {:Request-URI "/nachos"})))))

(run-specs)
