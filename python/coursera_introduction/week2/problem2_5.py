"""
COMMENT: Note that this uses a pseudorandom number generator.  That means
that the list will be different for each person.  We issue the command 
random.seed(70) inside the function problem2_4() to insure that we generate the
same numbers that the grader expects. If you do this problem correctly, you 
should get the list of random numbers below.

Test run:

problem2_4()
[34.54884618961936, 31.470395203793395, 32.297169396656095, 30.681793552717807,
 34.97530360173135, 30.773219981037737, 33.36969776732032, 32.990127772708405, 
 33.57311858494461, 32.052629620057274]
""""""
Problem 2_5:
Let's do a small simulation. Suppose that you rolled a die repeatedly. Each 
time that you roll the die you get a integer from 1 to 6, the number of pips
on the die. Use random.randint(a,b) to simulate rolling a die 10 times and 
printout the 10 outcomes. The function random.randint(a,b) will
generate an integer (whole number) between the integers a and b inclusive.
Remember each outcome is 1, 2, 3, 4, 5, or 6, so make sure that you can get 
all of these outcomes and none other. Print the list, one item to a line so that
there are 10 lines as in the example run.  Make sure that it has 10 items 
and they are all in the range 1 through 6.  Here is one of my runs. In
the problem below I ask you to set the seed to 171 for the benefit of the 
auto-grader. In this example, that wasn't done and so your numbers will be 
different.  Note that the seed must be set BEFORE randint is used.

problem2_5()
4
5
3
1
4
3
5
1
6
3

"""
"""
Problem 2_5:
"""
import random

def problem2_5():
    """ Simulates rolling a die 10 times."""
    # Setting the seed makes the random numbers always the same
    # This is to make the auto-grader's job easier.
    random.seed(171)  # don't remove when you submit for grading
    for i in range(0,10):
        print(random.randint(1,6))

#%%