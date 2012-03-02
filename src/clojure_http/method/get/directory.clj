(ns clojure-http.method.get.directory
  (:import (java.io File))
  (:use clojure-http.method.get.filesystem
        clojure-http.utility.datetime
        clojure-http.response)
  (:use [clojure.contrib.io :only [copy input-stream reader]])
  (:use [pantomime.mime :only [mime-type-of]]))

; TODO how can I have this set in core or in project.clj and get the value from there?
(def root "public")

(defn- make-url [path filename]
  (str "<a href=\"" path "\">" filename "</a></br>"))

(defn- make-directory-index-listing-entry [file]
  (let [path (subs (.getPath file) (count root))]
    (make-url path (.getName file))))

(defn- make-directory-index-listing [filename file]
  (apply str (map make-directory-index-listing-entry (-> filename File. .listFiles))))

(defn- make-directory-page [filename file]
  (apply str
    (str "<html><head><title>" filename "</title></head><body>")
    (str (make-directory-index-listing filename file))
    (str "</body></html>")))

(defmethod filesystem :Directory [request-headers file filename out]
  (do
    (let [body (make-directory-page filename file)]
      (println (:HTTP-Version request-headers) "200 OK")
      (println "Content-Type: text/html")
      (println "Connection: close")
      (println "Date:" (datetime-in-gmt))
      (println "Server: clip-clop/0.1")
      (println "Content-Length:" (count body))
      (println "")
      (println body))))
