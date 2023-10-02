import sys
from celery import Celery

from read import logger_task


counter = 1
if len(sys.argv)>=1:
    counter = int(sys.argv[1])

for i in range(counter):
    logger_task.delay(f"test{i}")