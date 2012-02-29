(ns clojure-http.core-spec
  (:use [speclj.core]))

(defn true-or-false []
  true)

(describe "truthiness"
  (it "test if true-or-false returns true"
    (should (true-or-false))))

(run-specs)
