"""
Problem 1_5:
Write a function 'problem1_5(age)'. This function should use if-elif-else
statement to print out "Have a glass of milk." for anyone under 7; "Have
a coke." for anyone under 21, and "Have a martini." for anyone 21 or older.

Tip: Be careful about the ages 7 (a seven year old is not under 7) and 21.
Also be careful to make the phrases exactly as shown for the auto-grader.
"""
#%%
def problem1_5(age):
    if(age<7):
        print("Have a glass of milk.")
    elif(age<21):
        print("Have a coke.")
    else:
        print("Have a martini.")
#%%
"""
Test runs (3 of them). Note that the grader program will use different numbers:
problem1_5(5)
Have a glass of milk.

problem1_5(10)
Have a coke.

problem1_5(25)
Have a martini.

"""