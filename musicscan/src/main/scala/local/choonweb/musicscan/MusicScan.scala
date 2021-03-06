package local.choonweb.musicscan

import java.io._
import scala.io.Source
import java.util.Properties
import com.mongodb.casbah.Imports._

case class FileFound(relativePath : String, file : File)
case class ScanDone()

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

  // Load our configuration
  val jsonStream = getClass().getClassLoader().getResourceAsStream("choonweb.conf")
  val config = new Configuration(Source.fromInputStream(jsonStream))

  // Connect to MongoDB
  val mongoConn = MongoConnection(config.mongoHost)
  val mongoDB = mongoConn(config.mongoDatabase)

  // Build our actors
  val persister = new MongoPersister(mongoDB).start
  val extractor = new TagExtractor(persister).start
  val filter = new MongoUnchangedFilter(mongoDB("tracks"), extractor).start

  // Look for audio files
  val audioFilenameRx = "^.*(mp3|m4a|ogg)$(?i)".r
  val walker = new DirectoryWalker(rootDir)
  
  for(file <- walker if audioFilenameRx.findFirstIn(file.getName).isDefined) {
      var relativePath = rootDir.toURI().relativize(file.toURI()).getPath()
      filter ! FileFound(relativePath, file)
  }

  filter ! ScanDone()
}

// vim: set ts=4 sw=4 et:
