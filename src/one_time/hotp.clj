(ns one-time.hotp
  (:import java.nio.ByteBuffer
           javax.crypto.spec.SecretKeySpec
           javax.crypto.Mac)
  (:require [one-time.codec :as codec]))

(declare hmac-sha-digest)
(declare get-hmac-sha-type-string)

(defn get-token
  "Return a HOTP token (HMAC-Based One-Time Password Algorithm)
   based on a secret and a counter, as specified in
   https://tools.ietf.org/html/rfc4226"
  ([secret counter]
   ;; Use HMAC-SHA-1 as default when not provided
   (get-token secret counter :hmac-sha-1))
  ([secret counter hmac-sha-type]
   (let [digest (hmac-sha-digest secret counter hmac-sha-type)
         offset (bit-and (digest 19) 0xf)
         code (bit-or (bit-shift-left (bit-and (digest offset) 0x7f) 24)
                      (bit-shift-left (bit-and (digest (+ offset 1)) 0xff) 16)
                      (bit-shift-left (bit-and (digest (+ offset 2)) 0xff) 8)
                      (bit-and (digest (+ offset 3)) 0xff))]
     (rem code 1000000))))

(defn- hmac-sha-digest
  "Return the HMAC-SHA-xxx digest for a given secret, counter and a sha type."
  [secret counter hmac-sha-type]
  (let [secret-binary (codec/decode-binary secret)
        data (.array (.putLong (ByteBuffer/allocate 8) counter))
        hmac-sha-type-string (get-hmac-sha-type-string hmac-sha-type)
        spec (SecretKeySpec. secret-binary hmac-sha-type-string)
        sha-mac (let [mac (Mac/getInstance hmac-sha-type-string)]
                  (.init mac spec) ; mutating api
                  mac)
        hash (into [] (.doFinal sha-mac data))] ; mutating api
    hash))

(defn- get-hmac-sha-type-string
  "Return the correct hmac sha type string as expected by the library,
   with safe default of hmac-sha-1 in case of bad input."
  [hmac-sha-type]
  (let [hmac-sha-type-string {:hmac-sha-1   "HmacSHA1"
                              :hmac-sha-256 "HmacSHA256"
                              :hmac-sha-512 "HmacSHA512"}]
    (get hmac-sha-type-string hmac-sha-type "HmacSHA1")))
