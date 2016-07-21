(ns one-time.util-test
  (:require [one-time.util :as sut]
            [clojure.test :refer :all]))

(deftest random-bytes-test
  (testing "Type test for random bytes. Not that useful"
    (is (instance? (Class/forName "[B") (sut/random-bytes 10)))))
