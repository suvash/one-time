(ns one-time.totp
  (:import java.util.Date)
  (:require [one-time.hotp :as hotp]))

(declare counter-since-epoch)
(declare unix-timestamp)

(defn get-token
  "Return a TOTP token (Time-Based One-Time Password Algorithm)
   based on a secret and time, as specified in
   https://tools.ietf.org/html/rfc4226"
  ([secret]
   ;; Default parameters if not provided
   (get-token secret {:date (Date.) :time-step 30 :hmac-sha-type :hmac-sha-1}))
  ([secret {:keys [date time-step hmac-sha-type], :or {date (Date.) time-step 30 hmac-sha-type :hmac-sha-1}}]
   {:pre [(not-any? nil? [secret date time-step hmac-sha-type])]}
   ;; Get HOTP token with counter with given parameters
   (let [counter(counter-since-epoch date time-step)]
     (hotp/get-token secret counter hmac-sha-type))))

(defn- counter-since-epoch
  "Calculate the number of time steps until for a date-time since epoch given a time step length."
  [^Date date-time time-step-duration]
  (int (/ (unix-timestamp (.getTime date-time)) time-step-duration)))

(defn- unix-timestamp
  "Convert milliseconds since epoch to a Unix Timestamp."
  [milliseconds]
  (int (/ milliseconds 1000)))
