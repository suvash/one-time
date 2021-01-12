(ns one-time.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.test-helper :as th]
            [one-time.core :as ot]))

(deftest generate-secret-key-test
  (testing "Secret Key type and length test"
    (is (instance? String (ot/generate-secret-key)))
    (is (= 16 (count (ot/generate-secret-key))))))

(deftest get-totp-token-test
  (testing "TOTP token for secret and parameters test"
    (is (= 316320 (ot/get-totp-token "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")})))
    (is (= 472710 (ot/get-totp-token "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                         :time-step 20})))
    (is (= 258945 (ot/get-totp-token "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                         :time-step 40
                                                         :hmac-sha-type :hmac-sha-256})))))

(deftest is-valid-totp-token?-test
  (testing "TOTP token validation test"
    (not (ot/is-valid-totp-token? 000000 "4CYLIWVIEQVA5IVP"))
    (is  (ot/is-valid-totp-token? 316320 "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")}))
    (is  (ot/is-valid-totp-token? 472710 "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                             :time-step 20}))
    (is  (ot/is-valid-totp-token? 258945 "4CYLIWVIEQVA5IVP" {:date (th/parse-date "Mon May 19 12:25:11 UTC 2014")
                                                             :time-step 40
                                                             :hmac-sha-type :hmac-sha-256}))
    (is  (ot/is-valid-totp-token? 662524 "A4I774XAQM36J7IL" {:date (th/parse-date "Thu Jul 23 09:31:37 UTC 2020")
                                                             :time-step 30
                                                             :time-step-offset 1}))))

(deftest get-hotp-token-test
  (testing "HOTP token for secret and counter test"
    (is (= 576304 (ot/get-hotp-token "4CYLIWVIEQVA5IVP" 1)))
    (is (= 740343 (ot/get-hotp-token "4CYLIWVIEQVA5IVP" 2)))
    (is (= 440272 (ot/get-hotp-token "4CYLIWVIEQVA5IVP" 3)))))

(deftest is-valid-hotp-token?-test
  (testing "HOTP token validation test"
    (is  (ot/is-valid-hotp-token? 576304 "4CYLIWVIEQVA5IVP" 1))
    (is  (ot/is-valid-hotp-token? 740343 "4CYLIWVIEQVA5IVP" 2))
    (is  (ot/is-valid-hotp-token? 440272 "4CYLIWVIEQVA5IVP" 3))))
