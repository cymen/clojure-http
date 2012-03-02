(ns clojure-http.utility.log
  (:use [clojure.contrib.io :only [writer]]))

(defn log
  ([message]
   (log message (writer System/out)))
  ([message out]
    (binding [*out* out]
      (println message))))
