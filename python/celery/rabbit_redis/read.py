from celery import Celery

rabbit_broker_url = "amqp://localhost:5672//"
redis_broker_url='redis://localhost:6379/0'
app = Celery('read', broker=redis_broker_url)

@app.task
def logger_task(message:str)->None:
    print (f"show the message {message}")