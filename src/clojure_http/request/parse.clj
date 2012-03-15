(ns clojure-http.request.parse
  (:use [clojure.string :as string :only [split]]
        [clojure-http.utility.stream :as stream]))

(defn parse-key-value [line]
  (rest (re-matches #"([^:]+): (.+)" line)))

(defn parse-key-value-into [collection line]
  (let [pair (parse-key-value line)]
    (assoc collection (keyword (first pair)) (second pair))))

(defn parse-request-line [line]
  (zipmap [:Method, :Request-URI, :HTTP-Version] (string/split line #"\s+")))

(defn parse-request-headers [lines]
  (reduce parse-key-value-into {} lines))

(defn parse-request [in]
  (let [request-header-lines (stream/readline-until-emptyline in)]
    (do
      (merge
        (parse-request-line (first request-header-lines))
        (parse-request-headers (rest request-header-lines))))))
