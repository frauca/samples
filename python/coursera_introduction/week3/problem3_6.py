# -problem3_1.py *- coding: utf-8 -*-

import sys

# add your code here

def readLinesWriteLengh(filein,fileout):
    f = open(filein)
    o = open(fileout,'w')
     
    for line in f:
        o.write("%d\n" % (len(line.strip("\n\r"))))
    f.close()
    o.close()
    
readLinesWriteLengh(sys.argv[1],sys.argv[2])