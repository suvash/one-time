(ns one-time.core-test
  (:require [one-time.test-helper :as th]
            [one-time.core :as sut]
            [clojure.test :refer :all]))

(deftest generate-secret-key-test
  (testing "Secret Key type and length test"
    (is (instance? String (sut/generate-secret-key)))
    (is (= 16 (count (sut/generate-secret-key))))))

(deftest get-totp-token-test
  (testing "TOTP token for secret and parameters test"
    (is (= 316320 (sut/get-totp-token "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")})))
    (is (= 472710 (sut/get-totp-token "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                          :time-step 20})))
    (is (= 258945 (sut/get-totp-token "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                          :time-step 40
                                                          :hmac-sha-type :hmac-sha-256})))))

(deftest is-valid-totp-token?-test
  (testing "TOTP token validation test"
    (not (sut/is-valid-totp-token? 000000 "4CYLIWVIEQVA5IVP"))
    (is  (sut/is-valid-totp-token? 316320 "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")}))
    (is  (sut/is-valid-totp-token? 472710 "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                              :time-step 20}))
    (is  (sut/is-valid-totp-token? 258945 "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                              :time-step 40
                                                              :hmac-sha-type :hmac-sha-256}))))

(deftest get-hotp-token-test
  (testing "HOTP token for secret and counter test"
    (is (= 576304 (sut/get-hotp-token "4CYLIWVIEQVA5IVP" 1)))
    (is (= 740343 (sut/get-hotp-token "4CYLIWVIEQVA5IVP" 2)))
    (is (= 440272 (sut/get-hotp-token "4CYLIWVIEQVA5IVP" 3)))))

(deftest is-valid-hotp-token?-test
  (testing "HOTP token validation test"
    (is  (sut/is-valid-hotp-token? 576304 "4CYLIWVIEQVA5IVP" 1))
    (is  (sut/is-valid-hotp-token? 740343 "4CYLIWVIEQVA5IVP" 2))
    (is  (sut/is-valid-hotp-token? 440272 "4CYLIWVIEQVA5IVP" 3))))
