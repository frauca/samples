from django.conf.urls.defaults import *
from core.views import hello
from books import views 

handler500 = 'djangotoolbox.errorviews.server_error'

urlpatterns = patterns('',
    #('^_ah/warmup$', 'djangoappengine.views.warmup'),
    ('^$', 'django.views.generic.simple.direct_to_template',{'template': 'home.html'}),
    ('^hello/$',hello),
    (r'^search-form/$', views.search_form),
    (r'^search/$', views.search),
)
