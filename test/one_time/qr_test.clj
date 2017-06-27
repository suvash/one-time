(ns one-time.qr-test
  (:require [clojure.test :refer [deftest is]]
            [one-time.core :as otp]
            [one-time.qr :as qr]))

(deftest topt-qr-bytestream-returns-a-bytestream
  (let [secret (otp/generate-secret-key)
        bytestream (qr/topt-qr-bytestream {:label "company.org"
                                           :user "user@gmail.com"
                                           :secret secret})]
    (is (instance? java.io.ByteArrayOutputStream bytestream))))

(deftest hopt-qr-bytestream-returns-a-bytestream
  (let [secret (otp/generate-secret-key)
        bytestream (qr/hotp-qr-bytestream {:label "company.org"
                                           :user "user@gmail.com"
                                           :secret secret
                                           :counter 1})]
    (is (instance? java.io.ByteArrayOutputStream bytestream))))
