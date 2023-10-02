import redis

# Connect to Redis
redis_client = redis.StrictRedis(host='localhost', port=6379, db=0)

#read one message
message = redis_client.blpop('myqueue', timeout=0)
    
# The message is returned as a tuple with the first element being the queue name
# and the second element being the message itself
message = message[1].decode('utf-8')

print(f"Received: {message}")
    