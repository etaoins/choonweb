import json

MONGO_HOST = 'localhost'
MONGO_DATABASE = 'choonweb'
MUSIC_PREFIX = 'http://localhost:8080/'

def load(filename):
	global MONGO_HOST
	global MONGO_DATABASE
	global MUSIC_PREFIX

	with open(filename, 'r') as f:
		config_data = json.load(f)

		try:
			MONGO_HOST = config_data['mongo']['host']
		except KeyError:
			pass

		try:
			MONGO_DATABASE = config_data['mongo']['database']
		except KeyError:
			pass

		try:
			MUSIC_PREFIX = config_data['musicPrefix']
		except KeyError:
			pass
