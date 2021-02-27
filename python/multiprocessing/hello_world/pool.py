import multiprocessing
import pathlib
from multiprocessing import Lock, Pool
from multiprocessing.context import Process

current_dir = pathlib.Path()


def multiplyBy2(result_file):
    current_val = 1
    if result_file.exists():
        lines = result_file.open("r").readlines()
        if len(lines) > 0:
            current_val = int(lines[-1])
    result_file.open("a").write('\n' + str(current_val * 2))

def multiplyBy2_concurrent(result_file, lock):
    lock.acquire()
    try:
        multiplyBy2(result_file)
    finally:
        lock.release()

if __name__ == '__main__':
    lock = Lock()
    file = current_dir / 'mult.txt'
    with Pool(processes=multiprocessing.cpu_count()) as pool:
        pool.map(multiplyBy2_concurrent, args=(file, lock) )
