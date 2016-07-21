# One Time Password (TOTP and HOTP) library for Clojure.

[![Build Status](https://travis-ci.org/suvash/one-time.svg?branch=master)](https://travis-ci.org/suvash/one-time?branch=master)
[![License](https://img.shields.io/badge/license-EPL-blue.svg?style=flat)](https://github.com/suvash/one-time/blob/master/LICENSE)
[![Clojars Project](https://img.shields.io/clojars/v/one-time.svg)](https://clojars.org/one-time)

A Clojure library for generating one time passwords (HOTP & TOTP) as per [RFC 4226](http://tools.ietf.org/html/rfc4226) and [RFC 6238](http://tools.ietf.org/html/rfc6238).

This library has been tested to be compatible with :
* [Google Authenticator](https://github.com/google/google-authenticator) for [Android](https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2) and [iPhone](https://itunes.apple.com/en/app/google-authenticator/id388497605)
- [Authy](https://www.authy.com) for [Android](https://play.google.com/store/apps/details?id=com.authy.authy) and [iPhone](https://itunes.apple.com/en/app/authy/id494168017)

One time passwords are used by a lot of websites for [multi-factor authentication](https://www.youtube.com/watch?v=17rykTIX_HY). You can find a list of such websites [here](https://en.wikipedia.org/wiki/Google_Authenticator#Usage).


## Project Maturity

One-Time is a new library, however should be feature complete and fairly stable.

## Installation

### Leiningen:

Add the following to your `:dependencies` in `project.clj`.

    [one-time "0.1.0"]

### Maven

Make sure to add Clojars to your `pom.xml`:

``` xml
<repository>
  <id>clojars.org</id>
  <url>http://clojars.org/repo</url>
</repository>
```

Then add the following to Maven dependencies.

    <dependency>
      <groupId>one-time</groupId>
      <artifactId>one-time</artifactId>
      <version>0.1.0</version>
    </dependency>


### Gradle

    compile "one-time:one-time:0.1.0"


## Documentation

The frequently used functions (with industry-standard defaults) are present in the `one-time.core` namespace. However, they build on functions present in other namespaces which can always be overridden or extended as needed.

### Secret Key Generation

Secret key generation is the first step in being able to use TOTP/HOTP. A function is provided that generates random secret keys compatible with Google Authenticator and Authy.

```clojure
(require '[one-time.core :as ot])

(ot/generate-secret-key)
;= "Z3YMI77OFLQBPNT6"
```

### Time based One-time passwords (TOTP)

TOTP is based on HOTP with a timestamp replacing the incrementing counter. . `one-time.core` namespace provides two functions for working with TOTP, one for getting the TOTP token at a certain time and a predicate function for verifying.

You're most likely to use the predicate function for verifying tokens that you receive from the user (Google Authenticator/Authy)

```clojure
(require '[one-time.core :as ot])

;; Generate the key first
(def secret-key (ot/generate-secret-key))

;; At current time
(ot/get-totp-token secret-key)
;= 885510 - this will be different on your machine as it's based on time

;; Verify at current time and after 30 seconds
;; arguments are token-received, secret-key
(def current-token (ot/get-totp-token secret-key))
(ot/is-valid-totp-token? current-token secret-key)
;= true

;; Wait 30 secs
(Thread/sleep 30000)

;; Verify after 30 secs
;; arguments are token-received, secret-key, counter
(ot/is-valid-totp-token? current-token secret-key)
;= false
```

The functions above also accept additional map by which various aspects of TOTP token generation is affected. One can configure them to accept a specific time (instead of current time), a different time step (instead of 30 secs), and a different HMAC SHA function (instead of HMAC-SHA-1). Feel free to look more into `one-time.core` and `one-time.totp` namespace to configure these.

### HMAC based One-time passwords (HOTP)

HOTP is also better understood as counter based OTP. `one-time.core` namespace provides two functions for working with HOTP, one for getting the HOTP token at a certain counter and a predicate function for verifying.

You're most likely to use the predicate function for verifying tokens that you receive from the user.

```clojure
(require '[one-time.core :as ot])

;; Generate the key first, in this example i'll pick one
(def secret-key "HZSRH7AWHI6JV427")

;; At counter 1
(ot/get-hotp-token secret-key 1)
;= 817667

;; At counter 478
(ot/get-hotp-token secret-key 478)
;= 793369

;; Verify at counter 1567
;; arguments are token-received, secret-key, counter
(ot/is-valid-hotp-token? 446789 secret-key 1567)
;= false

;; Verify at counter 23456
;; arguments are token-received, secret-key, counter
(ot/is-valid-hotp-token? 13085 secret-key 23456)
;= true
```

### TOTP/HOTP URI generation
`one-time.uri` namespace provides additional functions for generating TOTP and HOTP URIs which can be further embed in QR codes to present to the user. A map is used to configure the URI generation functions.

```clojure
(require '[one-time.uri  :as oturi])

;; Generate the key first, in this example i'll pick one
(def secret-key "NBCVJLJHRKQEYLAD")

;; TOTP URI generation function accepts a map with following keys (non-optional) to generate the URI
;; :label  = Company.com ---- Name of the secret issuing firm, usually the company name
;; :user   = user@email.com - Name of the user intended to use the secret, usually the email address
;; :secret = secret key " --- Secret Key needed to be used
;;
;;
(oturi/totp-uri {:label "Company.com" :user "user@email.com" :secret secret-key})
;= "otpauth://totp/Company.com:user%40email.com?secret=NBCVJLJHRKQEYLAD&issuer=Company.com"

;; HOTP URI generation function accepts a map with following keys (non-optional) to generate the URI
;; :label  = Company.com ---- Name of the secret issuing firm, usually the company name
;; :user   = user@email.com - Name of the user intended to use the secret, usually the email address
;; :secret = secret key " --- Secret Key needed to be used
;; :counter = 123456 -------- Counter need to be used for HOTP
;;
(oturi/hotp-uri {:label "Company.com" :user "user@email.com" :secret secret-key :counter 123456})
;= "otpauth://hotp/Company.com:user%40email.com?secret=NBCVJLJHRKQEYLAD&issuer=Company.com&counter=123456"
```

The URIs generated above can then be embedded into QR codes that can be displayed to user.

### Working Example

Scan the following barcode with your phone, using Google Authenticator or Authy.

![QR Code for TOTP](https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FCompany.com%3Auser%2540email.com%3Fsecret%3DTA4WPSAGBMGXLFVI%26issuer%3DCompany.com)

Run the following in a REPL and compare the values on the REPL and your device. Make sure you don't have any clock drifts etc., especially if you're running it inside a VM/container.
```clojure
(require '[one-time.core :as ot])

;; Use the same secret key as the bar code
(def secret-key "TA4WPSAGBMGXLFVI")

;; Compare these values to the one you get on your device at the same time
(ot/get-totp-token secret-key)
```

### QR Code URL Generation - READ CAREFULLY !

As of now, this library only supports QR code URL generation using Google APIs. The QR codes are themselves hosted by Google, hence it is fair to assume that the encoded TOTP/HOTP URIs will be logged on their servers along with the image http requests. This is a MAJOR SECURITY RISK, especially given that the secret has tbe presented to the user without anybody snooping around.

In the future, it might be possible for this library to generate QR codes directly which can then be served and destryed immediately. However, there are no such plans as of now.

IF YOU HAVE READ THE ABOVE PROPERLY AND UNDERSTAND THE RISKS, feel free to continue with the QR Code URL generation.
```clojure
(require '[one-time.uri  :as oturi])

;; Generate the key first, in this example i'll pick one
(def secret-key "NBCVJLJHRKQEYLAD")

;; TOTP URL Generation requires the same map as required by the URI generation function
(oturi/totp-qrcode-url {:label "Company.com" :user "user@email.com" :secret secret-key})
;= "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FCompany.com%3Auser%2540email.com%3Fsecret%3DNBCVJLJHRKQEYLAD%26issuer%3DCompany.com"

;; HOTP URL Generation requires the same map as required by the URI generation function
(oturi/hotp-qrcode-url {:label "Company.com" :user "user@email.com" :secret secret-key :counter 123456})
;= "https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth%3A%2F%2Fhotp%2FCompany.com%3Auser%2540email.com%3Fsecret%3DNBCVJLJHRKQEYLAD%26issuer%3DCompany.com%26counter%3D123456"
```
The URLs generated above should display the correct QR code, which can be used to scan on a device.

## Supported Clojure Versions

One-Time has been tested to work against Clojure 1.6 and up. The most recent release is always recommended.

## Development

One-Time uses [Leiningen 2](https://github.com/technomancy/leiningen/blob/master/doc/TUTORIAL.md). Make sure you have it installed and then run tests against all supported Clojure versions using

    lein all test

Then create a branch and make your changes on it. Once you are done with your changes and all tests pass, submit
a pull request on GitHub.

## License

Copyright © 2016 Suvash Thapaliya

Distributed under the [Eclipse Public License](https://github.com/suvash/one-time/blob/master/LICENSE).
