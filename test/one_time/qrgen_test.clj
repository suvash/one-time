(ns one-time.qrgen-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.core :as otp]
            [one-time.qrgen :as qrgen]))

(deftest totp-qrgen-bytestream-returns-a-bytestream
  (testing "TOTP QRGEN code bytestream test"
    (let [secret (otp/generate-secret-key)
          bytestream (qrgen/totp-bytestream {:label "company.org"
                                             :user "user@gmail.com"
                                             :secret secret})]
      (is (instance? java.io.ByteArrayOutputStream bytestream)))))

(deftest hotp-qrgen-bytestream-returns-a-bytestream
  (testing "HOTP QRGEN code bytestream test"
    (let [secret (otp/generate-secret-key)
          bytestream (qrgen/hotp-bytestream {:label "company.org"
                                             :user "user@gmail.com"
                                             :secret secret
                                             :counter 1})]
      (is (instance? java.io.ByteArrayOutputStream bytestream)))))
