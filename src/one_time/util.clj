(ns one-time.util
  (:import java.security.SecureRandom))

(defn random-bytes
  "Generate a random byte array."
  [size]
  (let [bytes (byte-array size)]
    (.nextBytes (SecureRandom/getInstance "SHA1PRNG") bytes) ; mutating api
    bytes))
