import sbt._

class MusicScanProject(info: ProjectInfo) extends DefaultProject(info)
{
  // For tag scanning
  val downloadJavaNet = "download.java.net Maven Repo" at "http://download.java.net/maven/2/"
  val jaudiotagger = "org" % "jaudiotagger" % "2.0.3"

  // XXX: Not using %% here as there's no official 2.9.0.RC2 build
  val casbah = "com.mongodb.casbah" % "casbah_2.9.0.RC1" % "2.1.2"
}

// vim: set ts=4 sw=4 et:
