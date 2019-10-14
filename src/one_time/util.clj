(ns one-time.util
  (:import java.security.SecureRandom))

(defn random-bytes
  "Generate a random byte array."
  [size]
  (let [bytes (byte-array size)]
    (-> (SecureRandom.) (.nextBytes bytes)) ; mutating api
    bytes))
