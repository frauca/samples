import sys
import redis

# Connect to Redis
redis_client = redis.StrictRedis(host='localhost', port=6379, db=0)

message = sys.argv[1]
print(f"Sending '{message}'")
# Messages to send to the queue
redis_client.rpush('myqueue', message)


# Close the connection
redis_client.close()
