(ns one-time.test-helper
  (:import java.text.SimpleDateFormat))

(defn parse-date
  [string]
  (.parse (java.text.SimpleDateFormat. "EEE MMM d HH:mm:ss zzz yyyy") string))
