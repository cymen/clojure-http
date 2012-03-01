(ns clojure-http.parse-request
  (:import (java.net URLDecoder))
  (:use [clojure.string :only [split]])
  (:use clojure-http.log))

(defn readline-until-blank [in]
  (take-while
    (partial not= "")
      (repeatedly #(.readLine in))))

(defn char-seq [in]
  (map char
    (take-while
      (and (.ready in) (partial not= -1))
        (repeatedly #(.read in)))))

(defn parse-key-value-into [collection line]
  (let [pair (rest (re-matches #"([^:]+): (.+)" line))]
    (assoc collection (keyword (first pair)) (second pair))))

(defn parse-request-headers [in]
  (let [request-header-lines (readline-until-blank in)]
    (do
      (log (first request-header-lines))
      (merge
        (zipmap [:Method, :Request-URI, :HTTP-Version] (split (first request-header-lines) #"\s+"))
        (reduce parse-key-value-into {} (rest request-header-lines))))))

(defn parse-request-body [request-headers]
  (if (contains? request-headers :Content-Length)
    (URLDecoder/decode
      (apply str
        (take (#(Integer/parseInt (:Content-Length request-headers)))
          (char-seq *in*))))))
