(ns clojure-http.datetime
  (:use [clj-time.core :only [now]])
  (:use [clj-time.coerce :only [from-long]])
  (:use [clj-time.format :only [formatter unparse]]))

(def custom-formatter (formatter "EEE, dd MMM yyyy HH:mm:ss 'GMT'"))

(defn datetime-in-gmt
  ([] (unparse custom-formatter (now)))
  ([last-modified] (unparse custom-formatter (from-long last-modified))))
