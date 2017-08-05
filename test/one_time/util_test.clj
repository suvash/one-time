(ns one-time.util-test
  (:require [clojure.test :refer [deftest testing is]]
            [one-time.util :as util]))

(deftest random-bytes-test
  (testing "Type test for random bytes. Not that useful"
    (is (instance? (Class/forName "[B") (util/random-bytes 10)))))
