import web

urls = (
		r'/artists/', 'controllers.artists.List',
		r'/artists/(.+)/tracks/', 'controllers.artists.Tracks',
		r'/artists/(.+)/albums/', 'controllers.artists.Albums',
		r'/artists/(.+)/albums/(.+)/tracks', 'controllers.artists.AlbumTracks',
		r'/tracks/', 'controllers.tracks.List',
	)

app = web.application(urls, globals())

if __name__ == '__main__':
	app.run()
