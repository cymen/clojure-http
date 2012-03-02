(ns clojure-http.method-get
  (:import (java.io File))
  (:use clojure-http.method
        clojure-http.filesystem
        clojure-http.datetime)
  (:use [clojure.contrib.io :only [copy input-stream reader]])
  (:use [pantomime.mime :only [mime-type-of]])
  (:require clojure-http.get-directory
            clojure-http.get-file))

(def root "public")

(defn resolve-file [request-headers]
  (str root (:Request-URI request-headers)))

(defn make-url [path filename]
  (str "<a href=\"" path "\">" filename "</a></br>"))

(defn make-directory-index-listing-entry [file]
  (let [path (subs (.getPath file) (count root))]
    (make-url path (.getName file))))

(defn make-directory-index-listing [filename file]
  (apply str (map make-directory-index-listing-entry (-> filename File. .listFiles))))

(defn make-directory-page [filename file]
  (apply str
    (str "<html><head><title>" filename "</title></head><body>")
    (str (make-directory-index-listing filename file))
    (str "</body></html>")))

(defmethod method "GET" [request-headers out]
  (let [filename (resolve-file request-headers)]
    (let [file (-> filename File.)]
      (if (.exists file)
        ; TODO check for If-Modified-Since and return a 304 (Not Modified) if appropriate
        (if (.isFile file)
          (do
            (with-open [*in* (reader filename)]
              (println (:HTTP-Version request-headers) "200 OK")
              (println "Content-Type:" (mime-type-of filename))
              (println "Content-Length:" (-> filename File. .length))
              (println "Connection: close")
              (println "Date:" (datetime-in-gmt))
              (println "Last-Modified:" (datetime-in-gmt (.lastModified file)))
              (println "Accept-Ranges: none")
              (println "Server: clip-clop/0.1")
              (println "")
              (copy (input-stream filename) out)))
          (if (.isDirectory file)
              (do
                (let [body (make-directory-page filename file)]
                  (println (:HTTP-Version request-headers) "200 OK")
                  (println "Content-Type: text/html")
                  (println "Connection: close")
                  (println "Date:" (datetime-in-gmt))
                  (println "Server: clip-clop/0.1")
                  (println "Content-Length:" (count body))
                  (println "")
                  (println body)))))
        (do
          (println (:HTTP-Version request-headers) "404 Not Found")
          (println "Date:" (datetime-in-gmt))
          (println "Server: clip-clop/0.1")
          (println "Connection: close"))))))
