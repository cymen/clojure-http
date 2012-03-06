(ns clojure-http.method.get.directory
  (:import (java.io File))
  (:use clojure-http.method.get.filesystem
        clojure-http.utility.datetime
        clojure-http.response)
  (:use [clojure.contrib.io :only [copy input-stream reader writer]])
  (:use [pantomime.mime :only [mime-type-of]]))

; TODO how can I have this set in core or in project.clj and get the value from there?
(def root "public")

(defn- make-title [filename]
  (subs filename (count root)))

(defn- make-url-directory [path filename]
  (str "<a href=\"" path "\">" filename "/</a></br>"))

(defn- make-url-file [path filename]
  (str "<a href=\"" path "\">" filename "</a></br>"))

(defn- make-directory-index-listing-entry [file]
  (let [path (subs (.getPath file) (count root))]
    (if (.isDirectory file)
      (make-url-directory path (.getName file))
      (make-url-file path (.getName file)))))

(defn- make-directory-index-listing [filename file]
  (apply str (map make-directory-index-listing-entry (-> filename File. .listFiles))))

(defn- make-directory-page [filename file]
  (apply str
    (str "<html><head><title>" (make-title filename) "</title></head><body>")
    (str (make-directory-index-listing filename file))
    (str "</body></html>")))

(defmethod filesystem :Directory [request-headers file filename]
  (let [body (make-directory-page filename file)]
    (hash-map
      :Status-Line {
        :HTTP-Version (:HTTP-Version request-headers)
        :Status-Code 200
        :Status-Message "OK"
      }
      :Headers {
        :Content-Type "text/html"
        :Connection "close"
        :Date (datetime-in-gmt)
        :Last-Modified (datetime-in-gmt (.lastModified file))
        :Server "clip-clop/0.1"
        :Content-Length (count body)
      }
      :Body body)))
