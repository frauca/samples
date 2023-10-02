# run redis

```
docker run -d --name my-redis-container -p 6379:6379 -v /tmp/redis-data:/data redis:latest --appendonly yes
```


# redis keeps messages on pod restart 

```
docker run -d --name my-redis-container -p 6379:6379 -v /tmp/redis-data:/data redis:latest --appendonly yes
python redis/send.py test1
docker stop my-redis-container
docker start my-redis-container
python redis/read.py
```