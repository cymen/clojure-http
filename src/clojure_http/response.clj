(ns clojure-http.response
  (:use clojure-http.utility.log))

(defn unparse-status-line [status-line]
  (str (:HTTP-Version status-line) " " (:Status-Code status-line) " " (:Status-Message status-line)))

(defn unparse-headers [headers-map]
  (apply str
    (map #(str (subs (str (first %)) 1) ": " (second %) "\n") headers-map)))
