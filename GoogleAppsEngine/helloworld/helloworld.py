import webapp2

class MainPage(webapp2.RequestHandler):
  def get(self):
      self.response.headers['Content-Type'] = 'text/plain'
      print(self.request.params)
      if self.request.params['paream']:
		print(self.request.params['paream'])
      self.response.out.write('Hello, webapp World! ')

app = webapp2.WSGIApplication([('/', MainPage)],
                              debug=True)
