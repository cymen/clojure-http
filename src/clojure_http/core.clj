(ns clojure-http.core
  (:import (java.io File))
  (:import (java.net URLDecoder))
  (:use [clojure.contrib.server-socket  :only [create-server]])
  (:use [clojure.contrib.io             :only [copy input-stream reader writer]])
  (:use [clojure.string                 :only [split]])
  (:use [pantomime.mime                 :only [mime-type-of]])
  (:use [clj-time.core                  :only [now]])
  (:use [clj-time.coerce                :only [from-long]])
  (:use [clj-time.format                :only [formatter unparse]]))

(def port 5000)
(def root "webroot")
(def custom-formatter (formatter "EEE, dd MMM yyyy HH:mm:ss 'GMT'"))

(defn readline-until-blank [in]
  (take-while
    (partial not= "")
      (repeatedly #(.readLine in))))

(defn parse-key-value-into [collection line]
  (let [pair (rest (re-matches #"([^:]+): (.+)" line))]
    (assoc collection (keyword (first pair)) (second pair))))

(defn parse-request-headers [request-headers-lines]
  (merge
    (zipmap [:Method, :Request-URI, :HTTP-Version] (split (first request-headers-lines) #"\s+"))
    (reduce parse-key-value-into {} (rest request-headers-lines))))

(defn char-seq [in]
  (map char
    (take-while
      (and (.ready in) (partial not= -1))
        (repeatedly #(.read in)))))

(defn parse-request-body [request-headers]
  (if (contains? request-headers :Content-Length)
    (URLDecoder/decode
      (apply str
        (take (#(Integer/parseInt (:Content-Length request-headers)))
          (char-seq *in*))))))

(defmulti response
  (fn [request-headers *out*] (:Method request-headers)))

(defn resolve-file [request-headers]
  (str root (:Request-URI request-headers)))

(defn make-url [path filename]
  (str "<a href=\"" path "\">" filename "</a></br>"))

(defn make-directory-index-listing [file]
  (let [path (subs (.getPath file) (count root))]
    (make-url path (.getName file))))

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
              (println (:HTTP-Version request-headers) "200 OK")
              (println "Content-Type: text/html")
              (println "Connection: close")
              (println "")
              (println "<html><body>")
              (doseq [file (-> filename File. .listFiles)]
                (println (make-directory-index-listing file)))
              (println "</body></html>"))))
        (do
          (println "should be a 404 on GET" (:Request-URI request-headers)))))))

(defmethod response "HEAD" [request-headers *out*]
  (println "HEAD!"))

(defmethod response "POST" [request-headers *out*]
  (do
    (println (parse-request-body request-headers))))

(defn http-server []
  (letfn [(http [in out]
    (binding [*in* (reader in)
              *out* (writer out)]
      (let [request-headers
        (parse-request-headers (readline-until-blank *in*))]
        (response request-headers out)
        (flush)
      )
    ))]
    (create-server port http)))

(defn -main []
  (http-server))
