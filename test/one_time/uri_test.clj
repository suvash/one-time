(ns one-time.uri-test
  (:require [one-time.uri :as sut]
            [clojure.test :refer :all]))

(deftest totp-uri-test
  (testing "TOTP URL for given secret and parameters test"
    (is (= "otpauth://totp/SomeLabel:user%40example.com?secret=7JLYRUNAS3YYCQSA&issuer=SomeLabel"
           (sut/totp-uri {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA"})))))

(deftest hotp-uri-test
  (testing "TOTP URL for given secret and parameters test"
    (is (= "otpauth://hotp/SomeLabel:user%40example.com?secret=7JLYRUNAS3YYCQSA&issuer=SomeLabel&counter=4"
           (sut/hotp-uri {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA" :counter 4})))))

(deftest totp-qrcode-url-test
  (testing "TOTP QR Code URL for given secret and parameters test"
    (is (= "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FSomeLabel%3Auser%2540example.com%3Fsecret%3D7JLYRUNAS3YYCQSA%26issuer%3DSomeLabel"
           (sut/totp-qrcode-url {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA"})))
    (is (= "https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FSomeLabel%3Auser%2540example.com%3Fsecret%3D7JLYRUNAS3YYCQSA%26issuer%3DSomeLabel"
           (sut/totp-qrcode-url {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA" :qr-code-size "300x300"})))))

(deftest hotp-qrcode-url-test
  (testing "TOTP QR Code URL for given secret and parameters test"
    (is (= "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth%3A%2F%2Fhotp%2FSomeLabel%3Auser%2540example.com%3Fsecret%3D7JLYRUNAS3YYCQSA%26issuer%3DSomeLabel%26counter%3D44"
           (sut/hotp-qrcode-url {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA" :counter 44})))
    (is (= "https://chart.googleapis.com/chart?chs=300x300&cht=qr&chl=otpauth%3A%2F%2Fhotp%2FSomeLabel%3Auser%2540example.com%3Fsecret%3D7JLYRUNAS3YYCQSA%26issuer%3DSomeLabel%26counter%3D73"
           (sut/hotp-qrcode-url {:label "SomeLabel" :user "user@example.com" :secret "7JLYRUNAS3YYCQSA" :counter 73 :qr-code-size "300x300"})))))

