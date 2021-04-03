#!/bin/bash

docker run --rm  -d --name word-counter-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres
sleep 2
docker exec -it word-counter-postgres psql -U postgres postgres -c "create user hello password 'secret'"
docker exec -it word-counter-postgres psql -U postgres postgres -c "create database hello"
docker exec -it word-counter-postgres psql -U postgres postgres -c "grant all privileges on database hello to hello"

diesel migration run