(ns clojure-http.response
  (:use clojure-http.utility.datetime
        clojure-http.utility.log))

(defn add-default-headers [headers-map]
  (merge
    headers-map
    {
      :Server "clip-clop/0.3"
      :Connection "close"
      :Date (datetime-in-gmt)
    }))

(defn unparse-status-line [status-line]
  (str (:HTTP-Version status-line) " " (:Status-Code status-line) " " (:Status-Message status-line)))

(defn unparse-headers [headers-map]
  (apply str
    (map #(str (subs (str (first %)) 1) ": " (second %) "\n") headers-map)))
