FROM clojure:temurin-21-lein-2.11.2-bookworm-slim

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY ./project.clj /usr/src/app/
RUN lein deps

COPY . /usr/src/app
CMD ["lein", "run"]
