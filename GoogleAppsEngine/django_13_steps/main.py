import logging, os, sys
from google.appengine.dist import use_library
use_library('django', '1.3')

# Must set this env var *before* importing any part of Django
os.environ['DJANGO_SETTINGS_MODULE'] = '13_steps.settings'

# Google App Engine imports.
from google.appengine.ext.webapp import util

# Remove the standard version of Django.
for k in [k for k in sys.modules if k.startswith('django')]:
  del sys.modules[k]

# Force sys.path to have our own directory first, in case we want to import
# from it.
sys.path.insert(0, os.path.abspath(os.path.dirname(__file__)))

import django.core.handlers.wsgi
import django.db


def main():
  # Create a Django application for WSGI.
  application = django.core.handlers.wsgi.WSGIHandler()

  # Run the WSGI CGI handler with that application.
  util.run_wsgi_app(application)

if __name__ == '__main__':
  main()