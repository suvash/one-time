(ns one-time.qr
  (:require [one-time.uri :as otp-uri])
  (:import net.glxn.qrgen.core.image.ImageType
           net.glxn.qrgen.javase.QRCode))

(def ^:private image-types
  {:JPG ImageType/JPG
   :GIF ImageType/GIF
   :PNG ImageType/PNG
   :BMP ImageType/BMP})

(defn topt-qr-bytestream
  "Returns a java.io.ByteArrayOutputStream"
  [{:keys [image-type label user secret]
    :or {image-type :JPG}}]
  {:pre [(image-types image-type)]}
  (-> (otp-uri/totp-uri {:label label
                         :secret secret
                         :user user})
      (QRCode/from)
      (.to (image-types image-type))
      (.stream)))

(defn hotp-qr-bytestream
  "Returns a java.io.ByteArrayOutputStream"
  [{:keys [image-type label user secret counter]
    :or {image-type :JPG}}]
  {:pre [(image-types image-type)]}
  (-> (otp-uri/hotp-uri {:label label
                         :secret secret
                         :user user
                         :counter counter})
      (QRCode/from)
      (.to (image-types image-type))
      (.stream)))
