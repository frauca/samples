# run redis

```
docker run -d --name my-redis-container -p 6379:6379 -v /tmp/redis-data:/data redis:latest --appendonly yes
```