(ns one-time.qrgen
  (:require [one-time.uri :as uri])
  (:import net.glxn.qrgen.core.image.ImageType
           net.glxn.qrgen.javase.QRCode))

(def ^:private image-types
  {:JPG ImageType/JPG
   :GIF ImageType/GIF
   :PNG ImageType/PNG
   :BMP ImageType/BMP})

(defn totp-bytestream
  "Returns a java.io.ByteArrayOutputStream with the totp qrcode"
  [{:keys [image-type image-size label user secret]
    :or {image-type :JPG image-size 125}}]
  {:pre [(not-any? nil? [label user secret])
         (image-types image-type)]}
  (-> (^String uri/totp-uri {:label label
                             :secret secret
                             :user user})
      (QRCode/from)
      (.to (image-types image-type))
      (.withSize image-size image-size)
      (.stream)))

(defn hotp-bytestream
  "Returns a java.io.ByteArrayOutputStream with the hotp qrcode"
  [{:keys [image-type image-size label user secret counter]
    :or {image-type :JPG image-size 125}}]
  {:pre [(not-any? nil? [label user secret counter])
         (image-types image-type)]}
  (-> (^String uri/hotp-uri {:label label
                             :secret secret
                             :user user
                             :counter counter})
      (QRCode/from)
      (.to (image-types image-type))
      (.withSize image-size image-size)
      (.stream)))
