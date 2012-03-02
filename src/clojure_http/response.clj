(ns clojure-http.response)

(defn headers [header-map]
  (apply str
    (doseq [keyval header-map]
      (str (key keyval) ":" (val keyval) "\n"))))
