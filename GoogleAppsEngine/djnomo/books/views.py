from django.shortcuts import render_to_response
from django.http import HttpResponse, HttpResponseRedirect
from models import Book
from books.forms import BookForm

def search_form(request):
    return render_to_response('search_form.html')

def search(request):
    if 'q' in request.GET and request.GET['q']:
        q = request.GET['q']
        books = Book.all()
        return render_to_response('search_results.html',
        {'books': books, 'query': q})
    else:
        return HttpResponse('Please submit a search term.')

def edit_book(request):
    if request.method == 'POST':
        form = BookForm(request.POST)
        if form.is_valid():
            cd = form.cleaned_data
            book = Book(title=cd['title'])
            book.put()
            return   HttpResponseRedirect('/search-form/?q=all')
    else:
        form = BookForm()
    return render_to_response('book_edit.html', {'form': form})