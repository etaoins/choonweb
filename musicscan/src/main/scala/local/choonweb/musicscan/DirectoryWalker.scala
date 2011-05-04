package local.choonweb.musicscan

import java.io._
import scala.collection.Traversable

class DirectoryWalker(root : File) extends Traversable[File]{
  def foreach[U](f: (File) => U) {
    def scanDir(dir : File) {
      for(file <- dir.listFiles() if !file.isHidden()) {
        if (file.isDirectory) {
          scanDir(file)
        }
        else {
          f(file)
        }
      }
    }

    scanDir(root)
  }
}

// vim: set ts=4 sw=4 et:
