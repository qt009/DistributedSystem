start:
	docker-compose up -d

stop:
	docker-compose down

build:
	docker-compose down -v
	docker-compose build
	docker-compose up -d --force-recreate distributed_system
	docker-compose up -d

clean:
	docker rm -v --force distributed_system
	docker network rm TEST_NET_1
clean-all:
	docker system prune -a