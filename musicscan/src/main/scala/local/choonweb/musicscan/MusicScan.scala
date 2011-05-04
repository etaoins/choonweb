package local.choonweb.musicscan

import java.io._
import java.util.Properties
import com.mongodb.casbah.Imports._

object MusicScan extends App {
  def badUsage() {
    println("Usage: musicscan [directory]")
    sys.exit(-1)
  }

  if (args.length != 1) {
    badUsage
  }

  val rootDir = new File(args(0))

  if (!rootDir.isDirectory()) {
    // We must be rooted in a directory
    badUsage
  }

  // Load our properties file
  val props = new Properties();
  props.load(getClass().getClassLoader().getResourceAsStream("musicscan.properties"))
  val mongoHost = props.getProperty("mongoHost", "localhost")

  // Connect to MongoDB
  val mongoConn = MongoConnection(mongoHost)
  val mongoDB = mongoConn("choonweb")
  val trackColl = mongoDB("tracks")

  def isAudioFile(file : File) : Boolean = {
    // Look for things with audio-y extensions
    val AudioFilename = "^.*(mp3|m4a|ogg)$(?i)".r

    file.getName() match {
      case AudioFilename(_) => return true
      case _ => return false
    }
  }

  val persister = new MongoPersister(trackColl)
  val extractor = new TagExtractor(persister)
  val filter = new MongoUnchangedFilter(trackColl, extractor)
  val scanner = new DirectoryScanner(isAudioFile)

  persister.start
  extractor.start
  filter.start
  scanner.scan(rootDir, filter)
}

// vim: set ts=4 sw=4 et:
