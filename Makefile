start:
	docker-compose up -d --build

stop:
	docker-compose down

build:
	docker-compose down -v
	docker-compose up --build

build -d:
	docker-compose down -v
	docker-compose up --build -d
clean:
	docker rm -v --force distributed_system
	docker network rm TEST_NET_1
clean-all:
	docker system prune -a