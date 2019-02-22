# One Time Password (TOTP and HOTP) library for Clojure.

[![Build Status](https://travis-ci.org/suvash/one-time.svg?branch=master)](https://travis-ci.org/suvash/one-time?branch=master)
[![Coverage Status](https://coveralls.io/repos/suvash/one-time/badge.svg?branch=master)](https://coveralls.io/r/suvash/one-time?branch=master)
[![Clojars Project](https://img.shields.io/clojars/v/one-time.svg)](https://clojars.org/one-time)

A Clojure library for generating one time passwords (HOTP & TOTP) as per [RFC 4226](http://tools.ietf.org/html/rfc4226) and [RFC 6238](http://tools.ietf.org/html/rfc6238). One time passwords are used by a lot of websites for [multi factor / two factor authentication](https://www.youtube.com/watch?v=17rykTIX_HY). You can find a list of such websites [here](https://twofactorauth.org).

This library has been tested to be compatible with :
* [Google Authenticator](https://github.com/google/google-authenticator) for [Android](https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2) and [iPhone](https://itunes.apple.com/en/app/google-authenticator/id388497605)
- [Authy](https://www.authy.com) for [Android](https://play.google.com/store/apps/details?id=com.authy.authy) and [iPhone](https://itunes.apple.com/en/app/authy/id494168017)
- [Lastpass Authenticator](https://lastpass.com/auth/) for [Android](https://play.google.com/store/apps/details?id=com.authy.authy) and [iPhone](https://itunes.apple.com/us/app/lastpass-authenticator/id1079110004)


## Project Maturity

One-Time is a feature complete and fairly stable library, given the small surface area of it's intent. Bugfixes and dependency updates will be made as required.

## Installation

### Leiningen:

Add the following to your `:dependencies` in `project.clj`.

    [one-time "0.5.0"]

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
      <version>0.5.0</version>
    </dependency>


### Gradle

    compile "one-time:one-time:0.5.0"


## Documentation

The frequently used functions (with industry-standard defaults) are present in the `one-time.core` namespace. However, they build on functions present in other namespaces which can always be overridden or extended as needed. Documentation on functions in individual namespaces (for the latest release) is available at https://suvash.github.io/one-time/

### Secret Key Generation

Secret key generation is the first step in being able to use TOTP/HOTP. A function is provided by `one-time.core` namespace that generates random secret keys compatible with Google Authenticator, Authy and Lastpass Authenticator.

```clojure
(require '[one-time.core :as ot])

(ot/generate-secret-key)
;= "Z3YMI77OFLQBPNT6"
```

### Time based One-time passwords (TOTP)

TOTP is based on HOTP with a timestamp replacing the incrementing counter. `one-time.core` namespace provides two functions for working with TOTP, one for getting the TOTP token at a certain time and a predicate function for verifying.

You're most likely to use the predicate function for verifying tokens that you receive from the user (Google Authenticator/Authy/Lastpass Authenticator)

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

You're most likely to use the predicate function for verifying tokens that you receive from the user. HOTP is not supported by Google Authenticator, Authy or Lastpass Authenticator. (They all support TOTP.)

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

Scan the following barcode with your phone, using Google Authenticator, Authy or Lastpass Authenticator.

![QR Code for TOTP](https://chart.googleapis.com/chart?chs=200x200&cht=qr&chl=otpauth%3A%2F%2Ftotp%2FCompany.com%3Auser%2540email.com%3Fsecret%3DTA4WPSAGBMGXLFVI%26issuer%3DCompany.com)

Run the following in a REPL and compare the values on the REPL and your device. Make sure you don't have any clock drifts etc., especially if you're running it inside a VM/container.
```clojure
(require '[one-time.core :as ot])

;; Use the same secret key as the bar code
(def secret-key "TA4WPSAGBMGXLFVI")

;; Compare these values to the one you get on your device at the same time
(ot/get-totp-token secret-key)
```

### QR Code Image Generation

This library wraps over https://github.com/kenglxn/QRGen, to generate QR code images locally. The generated image can be read as a `java.io.File` or over a `java.io.ByteArrayoutputstream`. Optionally, image type (BMP,JPG,PNG,GIF) and image size can be provided.
```clojure
(require '[one-time.qrgen :as qrgen])

;; Generate the key first, in this example i'll pick one
(def secret-key "HTWU5NFLBMWY2MQS")

;; TOTP QRcode image file generation, default image size(125px square) and type(JPG)
(def qrcode-file (qrgen/totp-file {:image-type :BMP :label "company.org" :user "user@gmail.com" :secret secret}))

;; TOTP QRcode image stream generation, in GIF
(def qrcode-stream (qrgen/totp-stream {:image-type :GIF :label "company.org" :user "user@gmail.com" :secret secret}))

;; HOTP QRcode image file generation, 300px square in PNG
(def qrcode-file (qrgen/hotp-file {:image-type :PNG :image-size 300 :label "company.org" :user "user@gmail.com" :secret secret :counter 123}))

;; HOTP QRcode image stream generation
(def qrcode-stream (qrgen/hotp-stream {:label "company.org" :user "user@gmail.com" :secret secret :counter 123}))
```
## Supported Clojure Versions

One-Time has been tested to work against Clojure 1.6 and up. The most recent release is always recommended.

## Development

If you already have Leiningen on your machine, you should just be able to run `lein all test` as you would do on other leiningen projects.

As prefered by the author, you can also use the provided Makefile to run the tests. In that case, you'll need the following on your machine
- GNU Make ( Version 4.0 and up )
- [Docker Engine](https://docs.docker.com/engine/installation/) ( Version 17.06.1 and hopefully upwards )
- [Docker Compose](https://github.com/docker/compose/releases) ( Version 1.16.1 and hopefully upwards )


```
# Get help
$ make help

# Run tests
$ make test

# Stop and cleanup docker instances etc.
# make stop-clean
```

Then create a branch and make your changes on it. Once you are done with your changes and all tests pass, submit
a pull request on GitHub.

## Changelog

Please check the [CHANGELOG.md](https://github.com/suvash/one-time/blob/master/CHANGELOG.md) graph for now.

## Contributors

Please check the [contribution graph](https://github.com/suvash/one-time/graphs/contributors) graph for now.

## References

These resources were invaluable towards developing this library.

- https://github.com/djui/clj-otp
- http://nakkaya.com/2012/08/13/google-hotp-totp-two-factor-authentication-for-clojure/

## License

Copyright © 2019 Suvash Thapaliya

Distributed under the [Eclipse Public License](https://github.com/suvash/one-time/blob/master/LICENSE).
