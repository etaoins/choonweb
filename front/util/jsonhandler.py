import model
import bson

def default_handler(obj):
	if hasattr(obj, 'isoformat'):
		return obj.isoformat()
	elif isinstance(obj, model.Track):
		return obj.__dict__
	elif isinstance(obj, model.TrackTag):
		return obj.__dict__
	elif isinstance(obj, bson.objectid.ObjectId):
		return str(obj)
