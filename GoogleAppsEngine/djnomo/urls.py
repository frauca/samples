from django.conf.urls.defaults import *
from books import views
from django.contrib import admin
admin.autodiscover()

handler500 = 'djangotoolbox.errorviews.server_error'

urlpatterns = patterns('',
    ('^_ah/warmup$', 'djangoappengine.views.warmup'),
    ('^$', 'django.views.generic.simple.direct_to_template',
     {'template': 'home.html'}),
    (r'^search-form/$', views.search_form),
    (r'^edit-book/$', views.edit_book),
    (r'^search/$', views.search),
    (r'^admin/', include(admin.site.urls)),
)
