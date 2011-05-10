import pymongo
from bson.code import Code

# Get our connection, database and collection
connection = pymongo.Connection()
db = connection.choonweb
coll = db.tracks

class TrackTag(object):
	def __init__(self, mongo):
		self.artist = mongo['artist']
		self.album = mongo['album']
		self.title = mongo['title']

class TrackFile(object):
	def __init__(self, mongo):
		self.size = mongo['size']
		self.last_modified = mongo['lastModified']

class Track(object):
	def __init__(self, mongo):
		self.id = mongo['_id']
		self.duration = mongo['duration']
		self.seen = mongo['seen']
		self.tag = TrackTag(mongo['tag'])
		self.file = TrackFile(mongo['file'])
		self.path = mongo['path']

def all_tracks():
	return [Track(mongo_track) for mongo_track in coll.find()]

def all_artists():
	return coll.distinct('tag.artist')

def artist_albums(artist):
	result = db.command({'distinct': 'tracks', 'key': 'tag.album', 'query': {'tag.artist': artist}})
	return result['values']

def artist_album_tracks(artist, album):
	return [Track(mongo_track) for mongo_track in coll.find({'tag.artist': artist, 'tag.album': album})]

def artist_tracks(artist):
	return [Track(mongo_track) for mongo_track in coll.find({'tag.artist': artist})]
