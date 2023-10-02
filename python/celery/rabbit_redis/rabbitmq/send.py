import pika

# Establish a connection to RabbitMQ server
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

# Declare a queue named 'my_queue'
channel.queue_declare(queue='my_queue',durable=True, exclusive=False, auto_delete=False)
channel.confirm_delivery()

persistent = pika.BasicProperties(
            delivery_mode=pika.DeliveryMode.Persistent)
# Message to send
message = "Hello, RabbitMQ!"

# Publish the message to 'my_queue'
channel.basic_publish(exchange='', routing_key='my_queue', body=message,properties=persistent, mandatory=True)

print(f" [x] Sent: {message}")

# Close the connection
connection.close()