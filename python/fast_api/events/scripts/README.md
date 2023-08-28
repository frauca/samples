# Developer

To debug and other dev tools

```
pip install -r scripts/requirements/requirements-dev.txt 
```

## Runnable version

The project is designed and tested with docker. So it is designed to be packetized with docker. We will run it with Kubernetes.

we will use kind to make the installation

We will need to create a cluster

```
kind create cluster --name fairs-bg
docker build -t fairs-bg:0.0.1 -f scripts/docker/Dockerfile .
kind load docker-image fairs-bg:0.0.1 --name fairs-bg
```

Load chart

```
helm dependency build scripts/charts
helm upgrade --install --atomic fairs-bg  scripts/charts
kubectl port-forward service/fairs-bg-service 8000:8000
```

## Run in local to debug

By the moment I will use a docker compose but in the future we will use just one tool that will be kubernetes.

The project is configured to run with a postgres db. A sql little would also be an excellent option but I do have take the postgres so it runs with the same db everywhere.

So to run in your local:

```
source .venv/bin/activate
docker compose -f scripts/docker/compose.yaml up -d
fairs-bg
```

CRT+C to cancel

```
docker compose -f scripts/docker/compose.yaml down
```
