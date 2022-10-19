(ns one-time.uri
  (:use [ring.util.codec :only [url-encode]]))

(declare make-query-string)
(declare build-url)
(declare otp-uri)

(defn totp-uri
  "Generate a TOTP url for QR Code usage"
  [{:keys [label user secret], :as all}]
  {:pre [(not-any? nil? [label user secret])]}
  (otp-uri (conj {:type "totp"} all)))

(defn hotp-uri
  "Generate a TOTP url for QR Code usage"
  [{:keys [label user secret counter], :as all}]
  {:pre [(not-any? nil? [label user secret counter])]}
  (otp-uri (conj {:type "hotp"} all)))

(defn- otp-uri
  "Generate a OTP url with the following schema
   otpauth://TYPE/LABEL:USER?secret=KEY&issuer=LABEL
   ;; TYPE   : totp | hotp
   ;; LABEL  : Company.com
   ;; USER   : user@email.com
   ;; SECRET : secret key "
  [{:keys [type label user secret counter], :or {counter nil} :as all}]
  (let [params {:secret secret :issuer label :counter counter}
        clean-params (into {} (filter (comp some? val) params))
        base (format "otpauth://%s/%s:%s" type (url-encode label) (url-encode user))
        url (build-url base clean-params)]
    url))

(defn- make-query-string
  "Encode key-value query pairs to an URL encoded query string."
  [m & [encoding]]
  (let [s #(if (instance? clojure.lang.Named %) (name %) %)
        enc (or encoding "UTF-8")]
    (->> (for [[k v] m]
           (str (url-encode (s k) enc) "=" (url-encode (str v) enc)))
         (interpose "&")
         (apply str))))

(defn- build-url
  "Construct a valid URL given a base and query key-value pairs."
  [url-base query-map & [encoding]]
  (str url-base "?" (make-query-string query-map encoding)))
