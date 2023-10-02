import pika

# Establish a connection to RabbitMQ server
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()

# Declare the same queue 'my_queue'
channel.queue_declare(queue='my_queue',durable=True, exclusive=False, auto_delete=False)

def callback(ch, method, properties, body):
    print(f" [x] Received: {body}")

# Set up a consumer and define the callback function
channel.basic_consume(queue='my_queue', on_message_callback=callback, auto_ack=True)

print(' [*] Waiting for messages. To exit, press Ctrl+C')
channel.start_consuming()