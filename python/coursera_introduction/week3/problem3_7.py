"""Problem 3_7:
Write a function that would read a CSV file that looks like this, flowers.csv:

petunia,5.95
alyssum,3.95
begonia,5.95
sunflower,5.95
coelius,4.95

and look up the price of a flower and print out that price.  Remember to import
the necessary library.

Here is my run on the above CSV file:
problem3_7("flowers.csv","alyssum")
3.95

Solution starter:
"""
#%%
import csv
    
def problem3_7(csv_pricefile, flower):
    """Reads a CSV file and print it as a list of rows."""
    f = open(csv_pricefile)
    data = {}
    for row in csv.reader(f):
        data[row[0]]=float(row[1])
    print(data[flower])   
    f.close()
    
#%%

