(ns clojure-http.method.get.directory
  (:import (java.io File))
  (:use clojure-http.method.get.filesystem
        clojure-http.utility.datetime
        clojure-http.response)
  (:use [clojure-http.config :as config])
  (:use [pantomime.mime :only [mime-type-of]]))

(defn- make-title [filename]
  (subs filename (count config/root)))

(defn- make-url-directory [path filename]
  (str "<a href=\"" path "\">" filename "/</a></br>"))

(defn- make-url-file [path filename]
  (str "<a href=\"" path "\">" filename "</a></br>"))

(defn- make-directory-index-listing-entry [file]
  (let [path (subs (.getPath file) (count config/root))]
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
        :Last-Modified (datetime-in-gmt (.lastModified file))
        :Content-Length (count body)
      }
      :Body body)))
