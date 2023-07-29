
# Docker


will run in sql little

makde the image

```
docker build -t local-registry/save-users:0.0.1 -f scripts/docker/Dockerfile .
```

run the image

```
docker run --name save_users -p 8001:8001 --rm local-registry/save-users:0.0.1 
```

# Kubernetes

we will use kind to make the installation

We will need to create a cluster

```
kind create cluster --name save-user
kind load docker-image local-registry/save-users:0.0.1 --name save-user
```

Load chart

```
helm dependency build scripts/charts
helm upgrade --install --atomic save-user  scripts/charts
kubectl port-forward service/save-user-service 8001:8001
```