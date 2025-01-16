(defproject one-time "0.9.0-SNAPSHOT"
  :author "Suvash Thapaliya"
  :description "One Time Password (TOTP and HOTP) library for Clojure. TOTP/HOTP is widely used for Two factor / Multi Factor Authentication."
  :url "http://github.com/suvash/one-time"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [commons-codec "1.17.2"]
                 [ring/ring-codec "1.2.0"]
                 [io.nayuki/qrcodegen "1.6.0"]]
  :plugins      [[lein-ancient "1.0.0-RC4-SNAPSHOT"]]
  :profiles {:dev    {:plugins      [[lein-cloverage "1.2.4"]
                                     [lein-codox "0.10.8"]
                                     [dev.weavejester/lein-cljfmt "0.13.0"]
                                     [lein-ancient "1.0.0-RC4-SNAPSHOT"]]}
             :1.10   {:dependencies [[org.clojure/clojure "1.10.0"]]}
             :1.10.1 {:dependencies [[org.clojure/clojure "1.10.1"]]}
             :1.10.2 {:dependencies [[org.clojure/clojure "1.10.2"]]}
             :1.10.3 {:dependencies [[org.clojure/clojure "1.10.3"]]}
             :1.11   {:dependencies [[org.clojure/clojure "1.11.0"]]}
             :1.11.1 {:dependencies [[org.clojure/clojure "1.11.1"]]}
             :1.11.2 {:dependencies [[org.clojure/clojure "1.11.2"]]}
             :1.11.3 {:dependencies [[org.clojure/clojure "1.11.3"]]}
             :1.11.4 {:dependencies [[org.clojure/clojure "1.11.4"]]}
             :1.12.0 {:dependencies [[org.clojure/clojure "1.12.0"]]}
             :alpha  {:dependencies [[org.clojure/clojure "1.12.0-alpha1"]]}}
  :codox {:output-path "docs"
          :doc-files   ["README.md"]
          :source-uri  "https://github.com/suvash/one-time/blob/v{version}/{filepath}#L{line}"}
  :repositories [["jitpack" "https://jitpack.io"]
                 ["sonatype" {:url "https://oss.sonatype.org/content/repositories/releases"
                              :snapshots false
                              :releases {:checksum :fail :update :always}}]
                 ["snapshots" {:url "https://oss.sonatype.org/content/repositories/snapshots"}]]
  :aliases  {"all" ["with-profile" "+dev:+1.10:+1.10.1:+1.10.2:+1.10.3:+1.11:+1.11.1:+1.11.2:+1.11.3:+1.11.4:+1.12.0:+alpha"]}
  :global-vars {*warn-on-reflection* true})
