(ns clojure-http.utility.urldecode
  (:import (java.net URLDecoder)))

(defn decode [byte-sequence]
  (URLDecoder/decode
    (apply str
      (map char (byte-sequence)))))
