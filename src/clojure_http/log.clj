(ns clojure-http.log
  (:use [clojure.contrib.io :only [writer]]))

(defn log [message]
  (binding [*out* (writer System/out)]
    (println message)))
