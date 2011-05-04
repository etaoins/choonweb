package local.choonweb.musicscan

import scala.actors._
import com.mongodb.casbah.Imports._
import java.util.Date

class MongoUnchangedFilter(trackColl : MongoCollection, downstream : Actor) extends Actor {
  def act() {
    loop {
      react {
        case FileFound(relativePath, file) =>
          // Is there a file with the same path, size and last modified?          
          val queryBulder = MongoDBObject.newBuilder
          queryBulder += "path" -> relativePath
          queryBulder += "file.lastModified" -> new Date(file.lastModified)
          queryBulder += "file.size" -> file.length

          // We don't actually want any fields but we get _id anyway
          val existingTrack = trackColl.findOne(queryBulder.result, MongoDBObject("_id" -> 1))

          if (existingTrack.isEmpty) {
            downstream ! FileFound(relativePath, file)
          }

        case ScanDone() =>
          downstream ! ScanDone()
          exit()

        case other : AnyRef =>
          downstream ! other
      }
    }
  }
}

// vim: set ts=4 sw=4 et:
