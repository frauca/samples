import multiprocessing
from multiprocessing.context import Process
from multiprocessing import Queue
import time
import random
import sys

class Value:

    def __init__(self) -> None:
        super().__init__()
        self.value = random.randrange(1000)


class ProcessWithChannel:

    def __init__(self) -> None:
        super().__init__()
        self.queue = Queue()
        self.process = Process(target=sum, args=(self.queue,))
        self.process.start()

    def proces(self, value):
        self.queue.put(value)

    def terminate(self):
        self.process.terminate()

    def join(self):
        self.process.join()


def sum(q):
    amount = 0
    count = 0
    while True:
        if q.empty():
            time.sleep(1)
        else:
            count += 1
            amount += q.get().value
            if count % 50 == 0:
                print('Counter ' + str(count) + ' amount ' + str(amount))
                sys.stdout.flush()



if __name__ == '__main__':
    ctx = multiprocessing.get_start_method('spawn')
    ps = []
    for i in range(10):
       ps.append(ProcessWithChannel())

    for i in range(10000):
        ps[i % len(ps)].proces(Value())

    for i in range(10):
        ps[i].terminate()
        ps[i].join()
    sys.stdout.flush()