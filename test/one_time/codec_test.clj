(ns one-time.codec-test
  (:require [one-time.codec :as sut]
            [clojure.test :refer :all]))

(deftest encode-test
  (testing "String to Base32 string encoding"
    (is (= "ONXW2ZJNON2HE2LOM4======" (sut/encode "some-string")))))

(deftest decode-test
  (testing "Base32 string to string decoding"
    (is (= "some-string" (sut/decode "ONXW2ZJNON2HE2LOM4======")))))

(deftest codec-full-cycle-test
  (testing "Full cycle of encoding and decoding"
    (is (= "some-string" (sut/decode (sut/encode "some-string"))))))
