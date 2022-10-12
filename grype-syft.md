```shell
grype target/one-time-0.7.1-standalone.jar
 ✔ Vulnerability DB        [no update available]
 ✔ Indexed target/one-time-0.7.1-standalone.jar
 ✔ Cataloged packages      [21 packages]
 ✔ Scanned image           [0 vulnerabilities]
No vulnerabilities found
```

```shell
syft target/one-time-0.7.1-standalone.jar
 ✔ Indexed target/one-time-0.7.1-standalone.jar
 ✔ Cataloged packages      [21 packages]
NAME                    VERSION  TYPE
batik-awt-util          1.15     java-archive
batik-constants         1.15     java-archive
batik-css               1.15     java-archive
batik-dom               1.15     java-archive
batik-ext               1.15     java-archive
batik-i18n              1.15     java-archive
batik-shared-resources  1.15     java-archive
batik-svggen            1.15     java-archive
batik-util              1.15     java-archive
batik-xml               1.15     java-archive
clojure                 1.11.1   java-archive
commons-codec           1.15     java-archive
core                    2.6.0    java-archive
core                    3.5.0    java-archive
core.specs.alpha        0.2.62   java-archive
jai-imageio-core        1.4.0    java-archive
javase                  2.6.0    java-archive
javase                  3.5.0    java-archive
one-time                0.7.1    java-archive
ring-codec              1.2.0    java-archive
spec.alpha              0.3.218  java-archive
```