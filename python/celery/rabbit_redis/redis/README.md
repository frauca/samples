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

# redis two readers not lossing messages

```
docker run -d --name my-redis-container -p 6379:6379 -v /tmp/redis-data:/data redis:latest --appendonly yes
celery -A read worker --loglevel=INFO
celery -A read worker --loglevel=INFO
python send.py 10
```

one show 0,2,4,6,8 and the other 1,3,5,7,9