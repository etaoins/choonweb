import web
import json
import model
from mimerender import mimerender
from util.jsonhandler import default_handler

templates  = web.template.render('templates/')

def render_json(**args):
	return json.dumps(args, default=default_handler)

class List:
	@mimerender(
			default = 'json',
			json = render_json,
			xml = templates.tracklist
	)
	def GET(self):
		return {'tracks': model.all_tracks()}

