from django import forms

class BookForm(forms.Form):
    title = forms.CharField(min_length=6,max_length=15,label='Set the title')
                               