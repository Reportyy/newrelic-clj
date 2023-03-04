(ns io.github.rutledgepaulv.newrelic-clj.inject-test
  (:require [clojure.test :refer :all]
            [io.github.rutledgepaulv.newrelic-clj.inject :refer :all]
            [ring.core.protocols :as protos])
  (:import (java.io ByteArrayOutputStream)))


(defn response-body->string [response]
  (with-open [stream (ByteArrayOutputStream.)]
    (protos/write-body-to-stream (:body response) response stream)
    (.close stream)
    (String. (.toByteArray stream))))

(deftest content-injection
  (let [header            "<script>console.log('header');</script>"
        footer            "<script>console.log('footer');</script>"
        original          "<html><head></head><body></body></html>"
        response          {:headers {"Content-Type" "text/html"} :body original}
        injected-response (perform-injection response header footer)]
    (is (satisfies? protos/StreamableResponseBody (:body injected-response)))
    (is (= (format "<html><head>%s</head><body>%s</body></html>" header footer)
           (response-body->string injected-response)))))