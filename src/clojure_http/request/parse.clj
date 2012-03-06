(ns clojure-http.request.parse
  (:import (java.net URLDecoder))
  (:use [clojure.string :only [split]]))

(defn readline-until-blank [in]
  (take-while
    (partial not= "")
      (repeatedly #(.readLine in))))

(defn byte-seq
  ([in]
    (take-while
      (and (.ready in) (partial not= -1))
        (repeatedly #(.read in))))
  ([in length]
    (take length
      (byte-seq in))))

(defn char-seq [in]
  (map char (byte-seq in)))

(defn parse-key-value-into [collection line]
  (let [pair (rest (re-matches #"([^:]+): (.+)" line))]
    (assoc collection (keyword (first pair)) (second pair))))

(defn parse-request-headers [in]
  (let [request-header-lines (readline-until-blank in)]
    (if (not (empty? (clojure.string/trim (first request-header-lines))))
      (do
        (merge
          (zipmap [:Method, :Request-URI, :HTTP-Version] (split (first request-header-lines) #"\s+"))
          (reduce parse-key-value-into {} (rest request-header-lines)))))))
