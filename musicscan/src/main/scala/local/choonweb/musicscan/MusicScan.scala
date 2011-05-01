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
}

// vim: set ts=4 sw=4 et:
