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
  ([remote-host-address request response]
   (log remote-host-address request response (writer System/out)))
  ([remote-host-address request response out]
    (binding [*out* out]
      (println
        (str remote-host-address " - - [" (apache-datetime) "] \"" (request-line request) "\" " (response-line response))))))
