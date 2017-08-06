(ns one-time.uri-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.uri :as uri]))

(deftest totp-uri-test
  (testing "TOTP URL for given secret and parameters test"
    (is (= "otpauth://totp/SomeLabel:user%40example.com?secret=7JLYRUNAS3YYCQSA&issuer=SomeLabel"
           (uri/totp-uri {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA"})))))

(deftest hotp-uri-test
  (testing "TOTP URL for given secret and parameters test"
    (is (= "otpauth://hotp/SomeLabel:user%40example.com?secret=7JLYRUNAS3YYCQSA&issuer=SomeLabel&counter=4"
           (uri/hotp-uri {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA" :counter 4})))))
