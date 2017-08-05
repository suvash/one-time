(ns one-time.codec-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.codec :as codec]))

(deftest encode-test
  (testing "String to Base32 string encoding"
    (is (= "ONXW2ZJNON2HE2LOM4======" (codec/encode "some-string")))))

(deftest decode-test
  (testing "Base32 string to string decoding"
    (is (= "some-string" (codec/decode "ONXW2ZJNON2HE2LOM4======")))))

(deftest codec-full-cycle-test
  (testing "Full cycle of encoding and decoding"
    (is (= "some-string" (codec/decode (codec/encode "some-string"))))))
