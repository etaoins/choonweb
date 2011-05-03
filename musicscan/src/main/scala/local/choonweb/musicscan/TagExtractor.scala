package local.choonweb.musicscan

import scala.actors._
import org.jaudiotagger.audio._
import org.jaudiotagger.tag._

case class AudioFileScanned(relativePath : String, audioFile : AudioFile)
case class TagExtractionDone();

class TagExtractor(persister : Actor) extends Actor {
  def act() {

    loop {
      react {
        case FileFound(relativePath, file) =>
          try {
            val audioFile = AudioFileIO.read(file)
            persister ! AudioFileScanned(relativePath, audioFile)
          }
          catch {
            case e:exceptions.InvalidAudioFrameException =>
          }

        case ScanDone() =>
          persister ! TagExtractionDone();
          exit()
      }
    }
  }
}

// vim: set ts=4 sw=4 et:
