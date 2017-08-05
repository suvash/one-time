(ns one-time.qrgen-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.core :as ot]
            [one-time.qrgen :as qrgen]))

(deftest totp-bytestream-returns-a-bytestream
  (testing "TOTP QR code bytestream test"
    (let [secret (ot/generate-secret-key)
          bytestream (qrgen/totp-bytestream {:label "company.org"
                                             :user "user@gmail.com"
                                             :secret secret})]
      (is (instance? java.io.ByteArrayOutputStream bytestream)))))

(deftest totp-file-returns-a-file
  (testing "TOTP QR code image file test"
    (let [secret (ot/generate-secret-key)
          file (qrgen/totp-file {:label "company.org"
                                 :user "user@gmail.com"
                                 :secret secret})]
      (is (instance? java.io.File file)))))

(deftest hotp-bytestream-returns-a-bytestream
  (testing "HOTP QR code bytestream test"
    (let [secret (ot/generate-secret-key)
          bytestream (qrgen/hotp-bytestream {:label "company.org"
                                             :user "user@gmail.com"
                                             :secret secret
                                             :counter 1})]
      (is (instance? java.io.ByteArrayOutputStream bytestream)))))

(deftest hotp-file-returns-a-file
  (testing "HOTP QR code file test"
    (let [secret (ot/generate-secret-key)
          file (qrgen/hotp-file {:label "company.org"
                                 :user "user@gmail.com"
                                 :secret secret
                                 :counter 1})]
      (is (instance? java.io.File file)))))
