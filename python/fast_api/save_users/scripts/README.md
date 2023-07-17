
# Docker


will run in sql little

makde the image

```
docker build -t save_users -f scripts/docker/Dockerfile .
```

run the image

```
docker run -d --name save_users -p 8001:8001 save_users
```