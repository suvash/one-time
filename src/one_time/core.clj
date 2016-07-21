(ns one-time.core
  (:require [one-time.util  :as util]
            [one-time.codec :as codec]
            [one-time.totp  :as totp]
            [one-time.hotp  :as hotp]))

(defn generate-secret-key
  "Generate a secret key for use in TOTP/HOTP."
  []
  ;; Keys mush be of the length of the HMAC output
  (-> 10
      util/random-bytes
      codec/encode-binary))

(defn get-totp-token
  "Gets the TOTP token for the secret and parameters provided"
  ([secret]
   (totp/get-token secret))
  ([secret {:keys [date time-step hmac-sha-type], :as all}]
   (totp/get-token secret all)))

(defn is-valid-totp-token?
  "Checks if the presented totp token is valid against a secret and options"
  ([token secret]
   (== token (get-totp-token secret)))
  ([token secret options]
   (== token (get-totp-token secret options))))

(defn get-hotp-token
  "Gets the HOTP token for the secret and counter provided"
  [secret counter]
  (hotp/get-token secret counter))

(defn is-valid-hotp-token?
  "Checks if the presented hotp token is valid against a secret and counter"
  [token secret counter]
  (== token (get-hotp-token secret counter)))
