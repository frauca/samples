#!/bin/bash

docker exec -it rust-postgres psql -U postgres postgres -c "create user hello password 'secret'"
docker exec -it rust-postgres psql -U postgres postgres -c "create database hello"
docker exec -it rust-postgres psql -U postgres postgres -c "grant all privileges on database hello to hello"

diesel migration run