(ns one-time.qrgen
  (:require [one-time.uri :as uri])
  (:import [java.io ByteArrayOutputStream]
           [javax.imageio ImageIO]
           [io.nayuki.qrcodegen QrCode QrCode$Ecc]
           ))

(def ^:private image-types
  {:JPG "JPG"
   :GIF "GIF"
   :PNG "PNG"
   :BMP "BMP"})

(defn totp-stream
  "Returns a java.io.ByteArrayOutputStream with the totp qrcode"
  [{:keys [image-type image-size label user secret]
    :or   {image-type :JPG image-size 3}}]
  {:pre [(not-any? nil? [label user secret])
         (image-types image-type)]}
  (let [baos (ByteArrayOutputStream.)]
    (-> (^String uri/totp-uri {:label  label
                               :secret secret
                               :user   user})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (.toImage image-size 0)
        (ImageIO/write ^String (image-types image-type) baos))
    baos))

(defn totp-file
  "Returns a java.io.File with the totp qrcode"
  [{:keys [image-type image-size label user secret]
    :or   {image-type :JPG image-size 3}}]
  {:pre [(not-any? nil? [label user secret])
         (image-types image-type)]}
  (let [f (java.io.File/createTempFile "qrgen" (str "." (image-types image-type)))]
    (-> (^String uri/totp-uri {:label  label
                               :secret secret
                               :user   user})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (.toImage image-size 0)
        (ImageIO/write ^String (image-types image-type) f))
    f))

(defn hotp-stream
  "Returns a java.io.ByteArrayOutputStream with the hotp qrcode"
  [{:keys [image-type image-size label user secret counter]
    :or   {image-type :JPG image-size 3}}]
  {:pre [(not-any? nil? [label user secret counter])
         (image-types image-type)]}
  (let [baos (ByteArrayOutputStream.)]
    (-> (^String uri/hotp-uri {:label   label
                               :secret  secret
                               :user    user
                               :counter counter})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (.toImage image-size 0)
        (ImageIO/write ^String (image-types image-type) baos))
    baos))

(defn hotp-file
  "Returns a java.io.File with the hotp qrcode"
  [{:keys [image-type image-size label user secret counter]
    :or   {image-type :JPG image-size 3}}]
  {:pre [(not-any? nil? [label user secret counter])
         (image-types image-type)]}
  (let [f (java.io.File/createTempFile "qrgen" (str "." (image-types image-type)))]
    (-> (^String uri/hotp-uri {:label   label
                               :secret  secret
                               :user    user
                               :counter counter})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (.toImage image-size 0)
        (ImageIO/write ^String (image-types image-type) f))
    f))
