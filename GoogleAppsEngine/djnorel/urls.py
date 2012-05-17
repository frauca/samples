from django.conf.urls.defaults import *
from django.contrib import admin
from core.views import hello
from books import views 

admin.autodiscover()
handler500 = 'djangotoolbox.errorviews.server_error'

urlpatterns = patterns('',
    #('^_ah/warmup$', 'djangoappengine.views.warmup'),
    ('^$', 'django.views.generic.simple.direct_to_template',{'template': 'home.html'}),
    ('^hello/$',hello),
    (r'^admin/', include(admin.site.urls)),
    (r'^search-form/$', views.search_form),
    (r'^search/$', views.search),
)
