(ns clojure-http.response-spec
  (:use speclj.core
        clojure-http.response))

(describe "response"

  (it "adds default headers to a response"
    (let [response (add-default-headers {})]
      (should= true (contains? response :Server))
      (should= true (contains? response :Connection))
      (should= true (contains? response :Date))))

  (it "unparses a response status line"
    (let [status-line { :HTTP-Version "HTTP/1.1" :Status-Code "404" :Status-Message "Not Found" }
          unparsed    (unparse-status-line status-line)]
      (should= "HTTP/1.1 404 Not Found" unparsed)))

  (it "unparses a map to response headers"
    (let [headers   { :Key "Value" :Key2 "Value2" }
          unparsed  (unparse-headers headers)]
      (should= "Key: Value\nKey2: Value2\n" unparsed)))

)

(run-specs)
