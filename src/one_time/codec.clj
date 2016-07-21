(ns one-time.codec
  (:import org.apache.commons.codec.binary.Base32))

(defn encode-binary
  "Encodes a binary to base32 string."
  [data]
  (.encodeAsString (Base32.) data))

(defn encode
  "Encodes a string to base32 string."
  [string]
  (encode-binary (.getBytes string)))

(defn decode-binary
  "Decodes a base32 string to binary."
  [string]
  (.decode (Base32.) string))

(defn decode
  "Decodes a base32 string to string."
  [string]
  (String. (decode-binary string)))
