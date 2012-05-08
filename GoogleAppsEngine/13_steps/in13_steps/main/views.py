# Create your views here.

from django.http import HttpResponse

from in13_steps.main.models import Visitor

def main(request):
    visitor = Visitor()
    visitor.ip = request.META["REMOTE_ADDR"]
    visitor.put()

    result = ""
    visitors = Visitor.all()
    visitors.order("added_on")

    for visitor in visitors.fetch(limit=40):
        result += visitor.ip + "visited on " + unicode(visitor.added_on) + "u"

    return HttpResponse(result)