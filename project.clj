(defproject one-time "0.3.0"
  :author "Suvash Thapaliya"
  :description "One Time Password (TOTP and HOTP) library for Clojure. TOTP/HOTP is widely used for Two factor / Multi Factor Authentication."
  :url "http://github.com/suvash/one-time"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [commons-codec "1.10"]
                 [ring/ring-codec "1.0.1"]
                 [com.github.kenglxn.qrgen/javase "2.3.0"]]
  :profiles {:dev {:plugins      [[lein-cloverage "1.0.6"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :master {:dependencies [[org.clojure/clojure "1.9.0-master-SNAPSHOT"]]}}
  :repositories {"jitpack" "https://jitpack.io"
                 "sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                             :snapshots false
                             :releases {:checksum :fail :update :always}}
                 "sonatype-snapshots" {:url "http://oss.sonatype.org/content/repositories/snapshots"
                                       :snapshots true
                                       :releases {:checksum :fail :update :always}}}
  :aliases  {"all" ["with-profile" "+dev:+1.6:+1.7:+1.8:+master"]}
  :global-vars {*warn-on-reflection* true})
