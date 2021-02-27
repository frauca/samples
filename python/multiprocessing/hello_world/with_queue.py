import multiprocessing
from multiprocessing.context import Process
from multiprocessing import Queue
import time
import random

import mp as mp


def sum(q):
    amount = 0
    count = 0
    while True:
        if q.empty():
            time.sleep(1)
        else:
            count += 1
            amount += q.get()
            if count % 50 == 0:
                print('Counter ' + str(count) + ' amount ' + str(amount))


if __name__ == '__main__':
    ctx = multiprocessing.get_start_method('spawn')
    qs = []
    ps = []
    for i in range(10):
        q = Queue()
        p = Process(target=sum, args=(q,))
        p.start()
        ps.append(p)
        qs.append(q)

    for i in range(10000):
        r = random.randrange(1000)
        qs[i % len(qs)].put(r)

    for i in range(10):
        ps[i].terminate()
        ps[i].join()
