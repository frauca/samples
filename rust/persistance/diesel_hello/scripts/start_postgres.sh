#!/bin/bash

docker run --rm  -d --name rust-postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres