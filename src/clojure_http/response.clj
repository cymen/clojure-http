(ns clojure-http.response
  (:use [clj-time.core :only [now]])
  (:use [clj-time.coerce :only [from-long]])
  (:use [clj-time.format :only [formatter unparse]]))

(def custom-formatter (formatter "EEE, dd MMM yyyy HH:mm:ss 'GMT'"))

(defn now-in-gmt []
  (unparse custom-formatter (now)))

(defn last-modified-date-in-gmt [last-modified]
  (unparse custom-formatter (from-long last-modified)))
