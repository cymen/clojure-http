(ns clojure-http.method-spec
  (:use speclj.core
        clojure-http.method))

(describe "method"
  (it "responds to a GET request"
    (= (:Status-Code (method { :Method "GET" :Request-URI "/" })) 200))

;  (it "responds to a GET request for a file"
;    (let [filename "/index.html"]
;      (println (method :GET { Method "GET" :Request-URI filename }))))

)

(run-specs)
