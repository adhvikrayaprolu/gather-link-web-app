.PHONY: dev check stop logs

dev:
	docker compose up --build --wait

check:
	sh scripts/container/check.sh

stop:
	docker compose down

logs:
	docker compose logs -f app
