(ns one-time.qrgen-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.core :as ot]
            [one-time.qrgen :as qrgen]))

(deftest totp-stream-returns-a-stream
  (testing "TOTP QR code stream test"
    (let [secret (ot/generate-secret-key)
          stream (qrgen/totp-stream {:label "company.org"
                                     :user "user@gmail.com"
                                     :secret secret})]
      (is (instance? java.io.ByteArrayOutputStream stream)))))

(deftest totp-file-returns-a-file
  (testing "TOTP QR code image file test"
    (let [secret (ot/generate-secret-key)
          file (qrgen/totp-file {:label "company.org"
                                 :user "user@gmail.com"
                                 :secret secret})]
      (is (instance? java.io.File file)))))

(deftest hotp-stream-returns-a-stream
  (testing "HOTP QR code stream test"
    (let [secret (ot/generate-secret-key)
          stream (qrgen/hotp-stream {:label "company.org"
                                     :user "user@gmail.com"
                                     :secret secret
                                     :counter 1})]
      (is (instance? java.io.ByteArrayOutputStream stream)))))

(deftest hotp-file-returns-a-file
  (testing "HOTP QR code file test"
    (let [secret (ot/generate-secret-key)
          file (qrgen/hotp-file {:label "company.org"
                                 :user "user@gmail.com"
                                 :secret secret
                                 :counter 1})]
      (is (instance? java.io.File file)))))
