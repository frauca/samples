# Create your views here.
from django.http import HttpResponse
from django.template.loader import get_template
from django.template import Context

def hello(request):
    t=get_template('hello.html')
    html=t.render(Context({}));
    return HttpResponse(html)