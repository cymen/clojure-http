(ns clojure-http.request.parse
  (:use [clojure.string :as string :only [split]]))

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

(defn parse-key-value [line]
  (rest (re-matches #"([^:]+): (.+)" line)))

(defn parse-key-value-into [collection line]
  (let [pair (parse-key-value line)]
    (assoc collection (keyword (first pair)) (second pair))))

(defn parse-request [in]
  (let [request-header-lines (readline-until-blank in)]
    (if (and (not (nil? (first request-header-lines))) (not (empty? (clojure.string/trim (first request-header-lines)))))
      (do
        (merge
          (zipmap [:Method, :Request-URI, :HTTP-Version] (string/split (first request-header-lines) #"\s+"))
          (reduce parse-key-value-into {} (rest request-header-lines)))))))
