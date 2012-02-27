(ns clojure-http.core)
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])
(use 'clojure.contrib.server-socket)
(use 'clojure.contrib.io)
(defn http-server []
  (letfn [(http [in out]
                    (binding [*in* (BufferedReader. (InputStreamReader. in))
                              *out* (OutputStreamWriter. out)]
                      (loop [line (read-line)]
                             (println line)
                             (if (not (= line ""))
                               (recur (read-line))))
                      ))]
    (create-server 5000 http)))
(defn -main []
  (http-server))
