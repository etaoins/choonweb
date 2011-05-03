package local.choonweb.musicscan

import java.io._
import java.util.Properties

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

  def isAudioFile(file : File) : Boolean = {
    // Look for things with audio-y extensions
    val AudioFilename = "^.*(mp3|m4a|ogg)$(?i)".r

    file.getName() match {
      case AudioFilename(_) => return true
      case _ => return false
    }
  }

  // Load our properties file
  val props = new Properties();
  props.load(getClass().getClassLoader().getResourceAsStream("musicscan.properties"))
  val mongoHost = props.getProperty("mongoHost", "localhost")

  val persister = new MongoPersister(mongoHost)
  val extractor = new TagExtractor(persister)
  val scanner = new DirectoryScanner(isAudioFile)

  persister.start
  extractor.start
  scanner.scan(rootDir, extractor)
}

// vim: set ts=4 sw=4 et:
