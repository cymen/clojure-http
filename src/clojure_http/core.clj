(ns clojure-http.core
  (:import (java.io File))
  (:use [clojure-http.parse-request])
  (:use [clojure-http.method-head])
  (:use [clojure.contrib.server-socket  :only [create-server]])
  (:use [clojure.contrib.io             :only [copy input-stream reader writer]])
  (:use [pantomime.mime                 :only [mime-type-of]])
  (:use [clj-time.core                  :only [now]])
  (:use [clj-time.coerce                :only [from-long]])
  (:use [clj-time.format                :only [formatter unparse]]))

(def root "public")
(def custom-formatter (formatter "EEE, dd MMM yyyy HH:mm:ss 'GMT'"))

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

(defmulti response
  (fn [request-headers *out*] (:Method request-headers)))

(defmethod response "GET" [request-headers *out*]
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
              (println "Date:" (unparse custom-formatter (now)))
              (println "Last-Modified:" (unparse custom-formatter (from-long (.lastModified file))))
              (println "Accept-Ranges: none")
              (println "Server: clip-clop/0.1")
              (println "")
              (copy (input-stream filename) *out*)
              (flush)))
          (if (.isDirectory file)
              (do
                (let [body (make-directory-page filename file)]
                  (println (:HTTP-Version request-headers) "200 OK")
                  (println "Content-Type: text/html")
                  (println "Connection: close")
                  (println "Date:" (unparse custom-formatter (now)))
                  (println "Server: clip-clop/0.1")
                  (println "Content-Length:" (count body))
                  (println "")
                  (println body)))))
        (do
          (println (:HTTP-Version request-headers) "404 Not Found")
          (println "Date:" (unparse custom-formatter (now)))
          (println "Server: clip-clop/0.1")
          (println "Connection: close"))))))

(defmethod response "HEAD" [request-headers *out*]
  (println "HEAD!"))

(defmethod response "POST" [request-headers *out*]
  (do
    (println (:HTTP-Version request-headers) "200 OK")
    (println "Connection: close")
    (println "Date:" (unparse custom-formatter (now)))
    (println "Accept-Ranges: none")
    (println "Server: clip-clop/0.1")
    (println "")
    (println (parse-request-body request-headers))))

(defmethod response "PUT" [request-headers *out*]
  (do
    (println (:HTTP-Version request-headers) "200 OK")
    (println "Connection: close")
    (println "Date:" (unparse custom-formatter (now)))
    (println "Accept-Ranges: none")
    (println "Server: clip-clop/0.1")
    (println "")
    (println (parse-request-body request-headers))))

(defn http-server [port]
  (letfn [(http [in out]
    (binding [*in* (reader in)
              *out* (writer out)]
      (let [request-headers
        (parse-request-headers *in*)]
        (response request-headers out)
        (flush)
      )
    ))]
    (create-server port http)))

(defn -main []
  (http-server 5000))
