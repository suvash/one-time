(ns one-time.qr-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.core :as otp]
            [one-time.qr :as qr]))

(deftest totp-qr-bytestream-returns-a-bytestream
  (testing "TOTP QR code bytestream test"
    (let [secret (otp/generate-secret-key)
          bytestream (qr/totp-qr-bytestream {:label "company.org"
                                             :user "user@gmail.com"
                                             :secret secret})]
      (is (instance? java.io.ByteArrayOutputStream bytestream)))))

(deftest hotp-qr-bytestream-returns-a-bytestream
  (testing "HOTP QR code bytestream test"
    (let [secret (otp/generate-secret-key)
          bytestream (qr/hotp-qr-bytestream {:label "company.org"
                                             :user "user@gmail.com"
                                             :secret secret
                                             :counter 1})]
      (is (instance? java.io.ByteArrayOutputStream bytestream)))))
