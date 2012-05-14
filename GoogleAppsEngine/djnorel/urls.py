from django.conf.urls.defaults import *
from core.views import hello

handler500 = 'djangotoolbox.errorviews.server_error'

urlpatterns = patterns('',
    #('^_ah/warmup$', 'djangoappengine.views.warmup'),
    ('^$', 'django.views.generic.simple.direct_to_template',{'template': 'home.html'}),
    ('^hello/$',hello)
)
