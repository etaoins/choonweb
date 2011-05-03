package local.choonweb.musicscan

import scala.actors._
import org.jaudiotagger.tag._
import com.mongodb.casbah.Imports._
import java.util.Date

class MongoPersister(mongoHost : String) extends Actor {
  val mongoConn = MongoConnection(mongoHost)
  val mongoDB = mongoConn("choonweb")

  def act() {
    val trackColl = mongoDB("tracks")

    loop {
      react {
        case AudioFileScanned(relativePath, audioFile) =>
          def tagInformation(tag : org.jaudiotagger.tag.Tag) : MongoDBObject = {
            val tagBuilder = MongoDBObject.newBuilder
            tagBuilder += "artist" -> tag.getFirst(FieldKey.ARTIST) 

            try {
              tagBuilder += "album" -> tag.getFirst(FieldKey.ALBUM) 
            }
            catch {
              case e : java.lang.NullPointerException => // Epic don't care
            }

            tagBuilder += "title" -> tag.getFirst(FieldKey.TITLE) 

            return tagBuilder.result
          }

          def fileInformation(file : java.io.File) : MongoDBObject = {
            val fileBuilder = MongoDBObject.newBuilder
            fileBuilder += "size" -> file.length
            fileBuilder += "lastModifed" -> new Date(file.lastModified)
            return fileBuilder.result
          }

          try {
            // This is what we're upserting based on
            val indexBuilder = MongoDBObject.newBuilder
            indexBuilder += "path" -> relativePath
            val index = indexBuilder.result
            
            // Add the tag to a high level track object
            val trackBuilder = MongoDBObject.newBuilder
            trackBuilder += "tag" -> tagInformation(audioFile.getTag())
            trackBuilder += "file" -> fileInformation(audioFile.getFile())
            trackBuilder += "duration" -> audioFile.getAudioHeader().getTrackLength()
            trackBuilder += "seen" -> new Date()
            trackBuilder ++= index
            val track = trackBuilder.result

            trackColl.update(index, track, upsert = true, multi = false)
          }
          catch {
            case e : java.lang.NullPointerException => // Don't save the track
          }
        case TagExtractionDone() =>
          exit();
      }
    }
  }
}

// vim: set ts=4 sw=4 et:
