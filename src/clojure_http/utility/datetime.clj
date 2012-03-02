(ns clojure-http.utility.datetime
  (:use [clj-time.core :only [now]])
  (:use [clj-time.coerce :only [from-long]])
  (:use [clj-time.format :only [formatter parse unparse]]))

(def custom-formatter (formatter "EEE, dd MMM yyyy HH:mm:ss 'GMT'"))

(defn datetime-from-long [datetime-long]
  (from-long datetime-long))

(defn datetime-in-gmt
  ([] (unparse custom-formatter (now)))
  ([last-modified] (unparse custom-formatter (datetime-from-long last-modified))))

(defn parse-http-datetime [datetime]
  (parse custom-formatter datetime))
