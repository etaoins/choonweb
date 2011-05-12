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
			xml = templates.artistlist,
			txt = lambda artists: "\n".join(artists)
	)
	def GET(self):
		return {'artists': model.all_artists()}

class Albums:
	@mimerender(
			default = 'json',
			json = render_json,
			xml = templates.albumlist,
			txt = lambda albums: "\n".join(albums)
	)
	def GET(self, artist):
		albums = model.artist_albums(artist)

		if not albums:
			# Suspicious
			if not model.artist_exists(artist):
				raise web.webapi.NotFound()

		return {'albums': albums}

class Tracks:
	@mimerender(
			default = 'json',
			json = render_json,
			xml = templates.tracklist
	)
	def GET(self, artist):
		tracks = model.artist_tracks(artist)

		if not tracks:
			# This is a bit tricky
			# Because we only store tracks if no tracks matched the artists then by
			# definition the artist doesn't exist
			raise web.webapi.NotFound()

		return {'tracks': tracks}

class AlbumTracks:
	@mimerender(
			default = 'json',
			json = render_json,
			xml = templates.tracklist
	)
	def GET(self, artist, album):
		tracks = model.artist_album_tracks(artist, album)

		if not tracks:
			# See Tracks.GET for my reasoning here
			raise web.webapi.NotFound()

		return {'tracks': tracks}


