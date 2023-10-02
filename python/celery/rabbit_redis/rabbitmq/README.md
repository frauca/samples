# run rabbitmq

```
docker run -d --name my-rabbitmq-container -p 5672:5672 -v /tmp/rabbitmq:/var/lib/rabbitmq/mnesia rabbitmq:3
```

# rabbit keeps messages on pod restart 

```
docker run -d --name my-rabbitmq-container -p 5672:5672 -v /tmp/rabbitmq:/var/lib/rabbitmq/mnesia rabbitmq:3
python rabbitmq/send.py
docker stop my-rabbitmq-container
docker start my-rabbitmq-container
python rabbitmq/read.py
```

# two readers not lossing messages

```
docker run -d --name my-redis-container -p 6379:6379 -v /tmp/redis-data:/data redis:latest --appendonly yes
celery -A read worker --loglevel=INFO
celery -A read worker --loglevel=INFO
python send.py 10
```

one show 0,2,4,6,8 and the other 1,3,5,7,9

# automatic reconnection with celery

```
docker stop my-rabbitmq-container
docker start my-rabbitmq-container
celery -A read worker --loglevel=INFO
python send.py 10
```

10 messages shown