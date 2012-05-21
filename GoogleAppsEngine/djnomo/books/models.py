from google.appengine.ext import db

# Create your models here.

class Book(db.Model):
    title = db.StringProperty(required=True)
    
    def __unicode__(self):
        return self.title