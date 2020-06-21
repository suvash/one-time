FROM clojure:openjdk-15-lein-2.9.3-alpine

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY ./project.clj /usr/src/app/
RUN lein deps

COPY . /usr/src/app
CMD ["lein", "run"]
