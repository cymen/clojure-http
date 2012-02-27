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

(defn parse-request [request-lines]
  (let [request
    (merge
      (zipmap [:Method, :Request-URI, :HTTP-Version] (split (first request-lines) #"\s+"))
      (reduce parse-key-value-into {} (rest request-lines)))]
    (doseq [kv request]
      (println (first kv) " --> " (second kv)))
  )
request-lines)

(defn http-server []
  (letfn [(http [in out]
                    (binding [*in* (BufferedReader. (InputStreamReader. in))
                              *out* (OutputStreamWriter. out)]
                        (let [request
                          (parse-request (read-until-empty))]
                          (println "\nKABOOM!\n")
                          (println request)
                        )
                    ))]
    (create-server 5000 http)))

(defn -main []
  (http-server))
