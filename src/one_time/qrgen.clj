(ns one-time.qrgen
  (:require [one-time.uri :as uri])
  (:import [java.io ByteArrayOutputStream]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [io.nayuki.qrcodegen QrCode QrCode$Ecc]))

(def ^:private image-types
  {:JPG "JPG"
   :GIF "GIF"
   :PNG "PNG"
   :BMP "BMP"})

(defn- ^BufferedImage to-image
  "Renders a QrCode to a BufferedImage."
  ([qr scale border]
   (to-image qr scale border 0xFFFFFF 0x000000))
  ([^QrCode qr scale border light-color dark-color]
   {:pre [(pos? scale) (>= border 0)]}
   (let [size   (.size qr)
         dim    (* (+ size (* border 2)) scale)
         result (BufferedImage. dim dim BufferedImage/TYPE_INT_RGB)]
     (doseq [y (range dim)
             x (range dim)]
       (let [module-x (- (quot x scale) border)
             module-y (- (quot y scale) border)
             dark?    (.getModule qr module-x module-y)]
         (.setRGB result x y (if dark? dark-color light-color))))
     result)))

(defn totp-stream
  "Returns a java.io.ByteArrayOutputStream with the totp qrcode"
  [{:keys [image-type image-scale image-border label user secret]
    :or   {image-type :JPG image-scale 6 image-border 0}}]
  {:pre [(not-any? nil? [label user secret])
         (image-types image-type)]}
  (let [baos (ByteArrayOutputStream.)]
    (-> (^String uri/totp-uri {:label  label
                               :secret secret
                               :user   user})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (to-image image-scale image-border)
        (ImageIO/write ^String (image-types image-type) baos))
    baos))

(defn totp-file
  "Returns a java.io.File with the totp qrcode"
  [{:keys [image-type image-scale image-border label user secret]
    :or   {image-type :JPG image-scale 6 image-border 0}}]
  {:pre [(not-any? nil? [label user secret])
         (image-types image-type)]}
  (let [f (java.io.File/createTempFile "qrgen" (str "." (image-types image-type)))]
    (-> (^String uri/totp-uri {:label  label
                               :secret secret
                               :user   user})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (to-image image-scale image-border)
        (ImageIO/write ^String (image-types image-type) f))
    f))

(defn hotp-stream
  "Returns a java.io.ByteArrayOutputStream with the hotp qrcode"
  [{:keys [image-type image-scale image-border label user secret counter]
    :or   {image-type :JPG image-scale 6 image-border 0}}]
  {:pre [(not-any? nil? [label user secret counter])
         (image-types image-type)]}
  (let [baos (ByteArrayOutputStream.)]
    (-> (^String uri/hotp-uri {:label   label
                               :secret  secret
                               :user    user
                               :counter counter})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (to-image image-scale image-border)
        (ImageIO/write ^String (image-types image-type) baos))
    baos))

(defn hotp-file
  "Returns a java.io.File with the hotp qrcode"
  [{:keys [image-type image-scale image-border label user secret counter]
    :or   {image-type :JPG image-scale 6 image-border 0}}]
  {:pre [(not-any? nil? [label user secret counter])
         (image-types image-type)]}
  (let [f (java.io.File/createTempFile "qrgen" (str "." (image-types image-type)))]
    (-> (^String uri/hotp-uri {:label   label
                               :secret  secret
                               :user    user
                               :counter counter})
        (QrCode/encodeText QrCode$Ecc/HIGH)
        (to-image image-scale image-border)
        (ImageIO/write ^String (image-types image-type) f))
    f))
