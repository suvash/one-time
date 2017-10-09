.DEFAULT_GOAL:=help
SHELL:=/bin/bash

RUN_SERVICE:=one-time

DOCKER_COMPOSE:=docker-compose -f docker-compose.yml
DOCKER_COMPOSE_RUN:=$(DOCKER_COMPOSE) run --rm $(RUN_SERVICE)

.PHONY: run bash lein-repl test stop prune

help: ## Display this help
	$(info)
	@awk 'BEGIN {FS = ":.*##"} /^[a-zA-Z_-]+:.*?##/ { printf " \033[36m%-10s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

build:  ## (Force) Build the one-time docker image ( eg. when having changed project.clj deps etc.)
	$(DOCKER_COMPOSE) build

run: ## Run a one-off command in a new one-time service container. Specify using CMD (eg. make run-web CMD=lein test)
	$(if $(CMD), $(DOCKER_COMPOSE_RUN) $(CMD), $(error -- CMD must be set))

bash: CMD=/bin/bash
bash: run ## Spawn a bash shell for one-time service

lein-repl: CMD=lein repl
lein-repl: run ## Spawn a lein repl for one-time service

test: CMD=lein all test
test: run ## Run the test suite for one-time service

stop: ## Stop all the service containers
	$(DOCKER_COMPOSE) down

stop-clean: ## Stop all the service containers, cleanup project images, dangling/orphaned volumes
	$(DOCKER_COMPOSE) down --rmi local --volumes --remove-orphans

prune: ## Cleanup dangling/orphaned docker resources globally
	docker system prune --volumes -f
