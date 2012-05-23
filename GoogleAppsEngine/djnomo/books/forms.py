from django import forms

class BookForm(forms.Form):
    title = forms.CharField()
    
class ContactForm(forms.Form):
        subject = forms.CharField()
        e_mail = forms.EmailField()
        message = forms.CharField()                             