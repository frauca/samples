'''
Created on 16/05/2012

@author: Administrador
'''

from django.contrib import admin
from models import Publisher,Author,Book

admin.site.register(Publisher)
admin.site.register(Author)
admin.site.register(Book)