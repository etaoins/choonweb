import pymongo
from bson.code import Code
import uuid
from web.utils import memoize

# Get our connection, database and collection
connection = pymongo.Connection()
db = connection.choonweb
coll = db.tracks


class TrackTag(object):
	def __init__(self, mongo):
		self.artist = mongo['artist']
		self.album = mongo['album']
		self.title = mongo['title']

class Track(object):
	def __init__(self, mongo):
		self.id = mongo['_id']
		self.duration = mongo['duration']
		self.tag = TrackTag(mongo['tag'])
		self.path = mongo['path']

def direct_datastore_state_tag():
	"""Returns an opaque identifier for the current state of the data store"""
	scan_coll = db.scans
	last_scan = scan_coll.find_one(fields=['_id'], sort=[('finished', pymongo.DESCENDING)])
	return str(last_scan['_id'])

datastore_state_tag = memoize(direct_datastore_state_tag, expires=300)

def all_tracks():
	"""Return all tracks"""
	return [Track(mongo_track) for mongo_track in coll.find()]

def all_tracks_matching(keywords):
	"""Find all tracks matching a list of keywords"""
	matching = []

	for keyword in keywords:
		matching.append([Track(mongo_track) for mongo_track in coll.find({'keywords': keyword})])

	return matching

def all_artists():
	"""Return all artists"""
	return coll.distinct('tag.artist')

def artist_albums(artist):
	"""Return all albums for a given artist"""
	result = db.command({'distinct': 'tracks', 'key': 'tag.album', 'query': {'tag.artist': artist}})
	return result['values']

def artist_album_tracks(artist, album):
	"""Return all tracks for a given artist and album"""
	return [Track(mongo_track) for mongo_track in coll.find({'tag.artist': artist, 'tag.album': album})]

def artist_tracks(artist):
	"""Return all tracks for a given artist"""
	return [Track(mongo_track) for mongo_track in coll.find({'tag.artist': artist})]

def artist_exists(artist):
	"""Determines if an artist exists"""
	return bool(coll.find_one({'tag.artist': artist}, fields=[]))
