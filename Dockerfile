FROM clojure:openjdk-17-lein-2.9.8-slim-buster

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY ./project.clj /usr/src/app/
RUN lein deps

COPY . /usr/src/app
CMD ["lein", "run"]
