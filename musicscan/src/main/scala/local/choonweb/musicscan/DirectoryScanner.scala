package local.choonweb.musicscan

import java.io._
import scala.actors._

case class FileFound(relativePath : String, file : File)
case class ScanDone()

class DirectoryScanner(filter : (File) => Boolean) {
  def scan(root : File, consumer : Actor) {
    def scanDir(dir : File) {
      for(file <- dir.listFiles() if !file.isHidden()) {
        if (file.isDirectory) {
          scanDir(file)
        }
        else if (filter(file)) {
          var relativePath = root.toURI().relativize(file.toURI()).getPath()
          consumer ! FileFound(relativePath, file)
        }
      }
    }

    scanDir(root)
    consumer ! ScanDone()
  }
}

// vim: set ts=4 sw=4 et:
