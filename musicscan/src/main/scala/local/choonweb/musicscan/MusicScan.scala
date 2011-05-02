package local.choonweb.musicscan

import java.io._

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
    badUsage
  }

  def isAudioFile(file : File) : Boolean = {
    val AudioFilename = "^.*(mp3|m4a|ogg)$(?i)".r

    file.getName() match {
      case AudioFilename(_) => return true
      case _ => return false
    }
  }

  val extractor = new TagExtractor
  val scanner = new DirectoryScanner(isAudioFile)

  extractor.start
  scanner.scan(rootDir, extractor)
}

// vim: set ts=4 sw=4 et:
