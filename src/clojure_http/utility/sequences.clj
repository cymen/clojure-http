(ns clojure-http.utility.sequences)

(defn byte-seq
  ([in]
    (take-while
      (and (.ready in) (partial not= -1))
        (repeatedly #(.read in))))
  ([in length]
    (take length
      (byte-seq in))))

(defn char-seq [in]
  (map char (byte-seq in)))
