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
			xml = templates.artistlist
	)
	def GET(self):
		return {'artists': model.all_artists()}

class Albums:
	@mimerender(
			default = 'json',
			json = render_json,
			xml = templates.albumlist)
	def GET(self, artist):
		return {'albums': model.artist_albums(artist)}

class Tracks:
	@mimerender(
			default = 'json',
			json = render_json,
			xml = templates.tracklist
	)
	def GET(self, artist):
		return {'tracks': model.artist_tracks(artist)}

class AlbumTracks:
	@mimerender(
			default = 'json',
			json = render_json,
			xml = templates.tracklist
	)
	def GET(self, artist, album):
		return {'tracks': model.artist_album_tracks(artist, album)}


