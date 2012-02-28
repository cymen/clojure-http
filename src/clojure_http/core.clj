(ns clojure-http.core)
(import '[java.io BufferedInputStream BufferedReader File FileInputStream FileReader InputStreamReader OutputStreamWriter])
(use 'clojure.contrib.server-socket)
(use 'clojure.contrib.io)
(use 'clojure.string)
(use 'pantomime.core)

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
    (if (and (-> filename File. .exists) (not (-> filename File. .isDirectory)))
      (do
        (binding [*in* (FileReader. filename)]
            (println (:HTTP-Version request-headers) "200 OK")
            (println "Content-Type:" (mime-type-of filename))
            (println "Content-Length:" (-> filename File. .length))
            (println "")
            (copy (input-stream filename) *out*)
            (flush)))
      (if (-> filename File. .isDirectory)
        (do
          (println (:HTTP-Version request-headers) "200 OK")
          (println "Content-Type: text/html")
          (println "")
          (println "<html><body>")
          (doseq [file (-> filename File. .listFiles)]
            (println (make-directory-index-listing file)))
          (println "</body></html>"))
        (do
          (println "should be a 404 on GET" (:Request-URI request-headers)
          (flush)))))))

(defmethod response "HEAD" [request-headers *out*]
  (println "HEAD!"))

(defmethod response "POST" [request-headers *out*]
  (println "POST!"))

(defn http-server []
  (letfn [(http [in out]
    (binding [*in* (BufferedReader. (InputStreamReader. in))
              *out* (OutputStreamWriter. out)]
      (let [request-headers
        (parse-request-headers (read-until-empty))]
        (response request-headers out)
        (flush)
      )
    ))]
    (create-server port http)))

(defn -main []
  (http-server))
