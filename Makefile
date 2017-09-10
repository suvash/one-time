.DEFAULT_GOAL:=test
.PHONY: run bash lein-repl test stop prune

RUN_SERVICE:=one-time

run:
	$(if $(CMD), docker-compose run $(RUN_SERVICE) $(CMD), $(error -- CMD must be set))

bash: CMD=/bin/bash
bash: run

lein-repl: CMD=lein repl
lein-repl: run

test: CMD=lein all test
test: run

stop:
	docker-compose down

prune:
	docker system prune -f
