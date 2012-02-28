(ns clojure-http.core)
(import '[java.io File])
(import '[java.net URLDecoder])
(use '[clojure.contrib.server-socket :only(create-server)])
(use '[clojure.contrib.io :only (copy input-stream reader writer)])
(use '[clojure.string :only (split)])
(use '[pantomime.mime :only (mime-type-of)])

(def port 5000)
(def root "webroot")

(defn read-until-empty []
  (loop [line (read-line) acc []]
    (if (= line "")
      acc
      (recur (read-line) (conj acc line)))))

(defn parse-key-value-into [collection line]
  (let [pair (rest (re-matches #"([^:]+): (.+)" line))]
    (assoc collection (keyword (first pair)) (second pair))))

(defn parse-request-headers [request-headers-lines]
  (merge
    (zipmap [:Method, :Request-URI, :HTTP-Version] (split (first request-headers-lines) #"\s+"))
    (reduce parse-key-value-into {} (rest request-headers-lines))))

(defn XXparse-request-body [request-headers]
  (loop [c (.read *in*) acc []]
    (do
      (if (not (.ready *in*))
        acc
        (recur (.read *in*) (conj acc c))))))

(defn char-seq [in]
  (map char
    (take-while
      (partial not= -1)
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
        (if (.isFile file)
          (do
            (with-open [*in* (reader filename)]
              (println (:HTTP-Version request-headers) "200 OK")
              (println "Content-Type:" (mime-type-of filename))
              (println "Content-Length:" (-> filename File. .length))
              (println "Connection: close")
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
    (doseq [keyval request-headers]
      (println (key keyval) (val keyval)))
    (println (parse-request-body request-headers))))

(defn http-server []
  (letfn [(http [in out]
    (binding [*in* (reader in)
              *out* (writer out)]
      (let [request-headers
        (parse-request-headers (read-until-empty))]
        (response request-headers out)
        (flush)
      )
    ))]
    (create-server port http)))

(defn -main []
  (http-server))
