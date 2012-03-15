(ns clojure-http.utility.stream)

(defn readline-until-emptyline [in]
  (take-while
    (partial not= "")
      (repeatedly #(.readLine in))))
