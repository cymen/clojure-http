(ns clojure-http.core
  (:use clojure-http.request.parse
        clojure-http.method
        clojure-http.response
        clojure-http.utility.log)
  (:use [clojure.contrib.io :only [reader writer]]
        [server-socket :only [create-server]])
  (:require clojure-http.method.default
            clojure-http.method.get
            clojure-http.method.head
            clojure-http.method.post
            clojure-http.method.put)
  (:import (java.io ByteArrayOutputStream)))

(defn http-server [port]
  (letfn [(http [in out remote-host-address]
    (binding [*in* (reader in)
              *out* (writer out)]
      (do
        (let [request (parse-request-headers *in*)
              response (method request)]
          ;(log remote-host-address request response)
          (println (unparse-status-line (:Status-Line response)))
          (println (unparse-headers (:Headers response)))
          (if (contains? response :Body)
            (let [body (:Body response)]
              (if (fn? body)
                (body out)
                (println body)))))
        (. in close)
        (. out close))))]
    (create-server port http)))

(defn -main []
  (http-server 5000))
