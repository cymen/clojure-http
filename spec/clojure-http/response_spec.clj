(ns clojure-http.response-spec
  (:use [speclj.core])
  (:use [clojure-http.response])
  (:use [clj-time.core :only [date-time]])
  (:use [clj-time.coerce :only [to-long]]))

(describe "response"
  (it "converts UNIX timestamp to HTTP date format"
    (do
      (should
        (= "Tue, 09 Aug 1977 04:15:30 GMT"
          (datetime-in-gmt (to-long (date-time 1977 8 9 4 15 30))))))))

(run-specs)
