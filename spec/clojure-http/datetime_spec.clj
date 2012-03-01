(ns clojure-http.datetime-spec
  (:use [speclj.core])
  (:use [clojure-http.datetime])
  (:use [clj-time.core :only [date-time from-time-zone time-zone-for-offset]])
  (:use [clj-time.coerce :only [to-long]]))

(describe "datetime"
  (it "converts UNIX timestamp to HTTP date format"
    (should
      (= "Tue, 09 Aug 1977 09:15:30 GMT"
        (datetime-in-gmt
          (to-long
            (from-time-zone
              (date-time 1977 8 9 4 15 30)
              (time-zone-for-offset -5))))))))

(run-specs)
