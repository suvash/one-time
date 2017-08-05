(ns one-time.totp-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.test-helper :as th]
            [one-time.totp :as totp]))

(deftest get-token-test
  (testing "TOTP token for parameters test"
    (is (= 319222 (totp/get-token "A4I774XAQM36J7IL" {:date (th/parse-date "Thu Jul 21 01:12:31 UTC 2016")})))
    (is (= 319222 (totp/get-token "A4I774XAQM36J7IL" {:date (th/parse-date "Thu Jul 21 01:12:31 UTC 2016")
                                                      :time-step 30})))
    (is (= 319222 (totp/get-token "A4I774XAQM36J7IL" {:date (th/parse-date "Thu Jul 21 01:12:31 UTC 2016")
                                                      :time-step 30 :hmac-sha-type
                                                      :hmac-sha-1})))

    (is (= 135921 (totp/get-token "A4I774XAQM36J7IL" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")})))
    (is (= 683074 (totp/get-token "A4I774XAQM36J7IL" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                      :time-step 20})))
    (is (= 170002 (totp/get-token "A4I774XAQM36J7IL" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                      :time-step 40
                                                      :hmac-sha-type :hmac-sha-256})))
    (is (= 998884 (totp/get-token "A4I774XAQM36J7IL" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                      :time-step 60
                                                      :hmac-sha-type :hmac-sha-512})))))
