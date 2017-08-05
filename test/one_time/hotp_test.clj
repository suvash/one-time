(ns one-time.hotp-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.hotp :as hotp]))

(deftest get-token-test
  (testing "HOTP token for parameters test"
    (is (= 336408 (hotp/get-token "7LE26MJNFRWHLGKQ" 1)))
    (is (= 336408 (hotp/get-token "7LE26MJNFRWHLGKQ" 1 :hmac-sha-1)))
    (is (= 101344 (hotp/get-token "7LE26MJNFRWHLGKQ" 1 :hmac-sha-256)))
    (is (= 913073 (hotp/get-token "7LE26MJNFRWHLGKQ" 1 :hmac-sha-512)))
    (is (= 58152  (hotp/get-token "7LE26MJNFRWHLGKQ" 2)))
    (is (= 58152  (hotp/get-token "7LE26MJNFRWHLGKQ" 2 :hmac-sha-1)))
    (is (= 579423 (hotp/get-token "7LE26MJNFRWHLGKQ" 2 :hmac-sha-256)))
    (is (= 811165 (hotp/get-token "7LE26MJNFRWHLGKQ" 2 :hmac-sha-512)))))
