(ns clojure-http.utility.datetime
  (:use [clj-time.core :only [now]])
  (:use [clj-time.coerce :only [from-long]])
  (:use [clj-time.format :only [formatter parse unparse]]))

(def http-date-format (formatter "EEE, dd MMM yyyy HH:mm:ss 'GMT'"))
(def apache-log-format (formatter "dd/MMM/yyyy:hh:mm:ss Z"))

(defn datetime-from-long [datetime-long]
  (from-long datetime-long))

(defn datetime-in-gmt
  ([]
    (unparse http-date-format (now)))
  ([datetime-long]
    (unparse http-date-format (datetime-from-long datetime-long))))

(defn parse-http-datetime [datetime]
  (parse http-date-format datetime))

(defn apache-datetime
  ([]
    (unparse apache-log-format (now)))
  ([datetime-long]
    (unparse apache-log-format (datetime-from-long datetime-long))))
