package local.choonweb.musicscan

import scala.actors._
import scala.concurrent.ops._
import org.jaudiotagger.audio._
import org.jaudiotagger.tag._

class TagExtractor extends Actor {
  def act() {

    // Used 

    loop {
      react {
        case FileFound(file) =>
          try {
            val audioFile = AudioFileIO.read(file)
            val tag = audioFile.getTag()
          }
          catch {
            case e:exceptions.InvalidAudioFrameException =>
          }

        case ScanDone() =>
          exit()
      }
    }
  }
}

// vim: set ts=4 sw=4 et:
