import web
import model
import settings

urls = (
		r'/artists/', 'controllers.artists.List',
		r'/artists/(.+)/tracks/', 'controllers.artists.Tracks',
		r'/artists/(.+)/albums/', 'controllers.artists.Albums',
		r'/artists/(.+)/albums/(.+)/tracks', 'controllers.artists.AlbumTracks',
		r'/tracks/', 'controllers.tracks.List',
	)

def check_modified(handler):
	web.http.expires(300)
	web.http.modified(etag=model.datastore_state_tag())
	return handler()

# Load our settings
settings.load("choonweb.conf")

# Connect to Mongo
model.connect()

app = web.application(urls, globals())

# As we're only a read only reflection of Mongo every can be checked against
# our MongoDB state
app.add_processor(check_modified)

if __name__ == '__main__':
	app.run()
