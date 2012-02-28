(ns clojure-http.core)
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])
(use 'clojure.contrib.server-socket)
(use 'clojure.contrib.io)
(use 'clojure.string)

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
  (fn [request-headers] (:Method request-headers)))

(defmethod response "GET" [request-headers]
  (println "GET!"))

(defmethod response "HEAD" [request-headers]
  (println "HEAD!"))

(defmethod response "POST" [request-headers]
  (println "POST!"))

(defn http-server []
  (letfn [(http [in out]
                    (binding [*in* (BufferedReader. (InputStreamReader. in))
                              *out* (OutputStreamWriter. out)]
                        (let [request-headers
                          (parse-request-headers (read-until-empty))]
                          (response request-headers)
                        )
                    ))]
    (create-server 5000 http)))

(defn -main []
  (http-server))
