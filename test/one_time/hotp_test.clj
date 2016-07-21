(ns one-time.hotp-test
  (:require [one-time.hotp :as sut]
            [clojure.test :refer :all]))

(deftest get-token-test
  (testing "HOTP token for parameters test"
    (is (= 336408 (sut/get-token "7LE26MJNFRWHLGKQ" 1)))
    (is (= 336408 (sut/get-token "7LE26MJNFRWHLGKQ" 1 :hmac-sha-1)))
    (is (= 101344 (sut/get-token "7LE26MJNFRWHLGKQ" 1 :hmac-sha-256)))
    (is (= 913073 (sut/get-token "7LE26MJNFRWHLGKQ" 1 :hmac-sha-512)))
    (is (= 58152  (sut/get-token "7LE26MJNFRWHLGKQ" 2)))
    (is (= 58152  (sut/get-token "7LE26MJNFRWHLGKQ" 2 :hmac-sha-1)))
    (is (= 579423 (sut/get-token "7LE26MJNFRWHLGKQ" 2 :hmac-sha-256)))
    (is (= 811165 (sut/get-token "7LE26MJNFRWHLGKQ" 2 :hmac-sha-512)))))
