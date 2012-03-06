(ns clojure-http.utility.log
  (:use clojure-http.utility.datetime)
  (:use [clojure.contrib.io :only [writer]]))

(defn- request-line [request]
  (str (:Method request) " " (:Request-URI request) " " (:HTTP-Version request)))

(defn- response-line [response]
  (if (:Content-Length (:Headers response))
    (str (:Status-Code (:Status-Line response)) " " (:Content-Length (:Headers response)))
    (str (:Status-Code (:Status-Line response)) " 0")))

(defn log
  ([message] (log message (writer System/out)))
  ([message out] (binding [*out* out] (println message))))

(defn log-request-response
  ([remote-host-address request response]
    (log-request-response remote-host-address request response (writer System/out)))
  ([remote-host-address request response out]
    (log (str remote-host-address " - - [" (apache-datetime) "] \"" (request-line request) "\" " (response-line response)) out)))
