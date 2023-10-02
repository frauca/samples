from celery import Celery


app = Celery('read', broker='redis://localhost:6379/0')

@app.task
def logger_task(message:str)->None:
    print (f"show the message {message}")