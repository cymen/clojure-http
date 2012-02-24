(ns clojure-http.core)
(import '[java.io BufferedReader InputStreamReader OutputStreamWriter])
(use 'clojure.contrib.server-socket)
(defn http-server []
  (letfn [(http [in out]
                    (binding [*in* (BufferedReader. (InputStreamReader. in))
                              *out* (OutputStreamWriter. out)]
                      (let [input (read-line)]
                        (print input)
                        (flush))))]
    (create-server 5000 http)))
(defn -main []
  (http-server))
