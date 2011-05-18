package local.choonweb.musicscan
import scala.util.parsing.json.JSON
import scala.io.Source

object Configuration {
  var mongoHost = "localhost"
  var mongoDatabase = "choonweb"
  var musicPrefix = "http://localhost:8080/"

  private val jsonStream = getClass().getClassLoader().getResourceAsStream("musicscan.conf")
  private val jsonText = Source.fromInputStream(jsonStream).mkString

  for(fullJson <- JSON.parseFull(jsonText)) fullJson match {
    case rootMap : Map[String, Any] =>
      for(pair <- rootMap) pair match {
        case ("musicPrefix", strValue : String) =>
          musicPrefix = strValue
        case ("mongo", mongoMap : Map[String, Any]) =>
          for(mongoPair <- mongoMap) mongoPair match {
            case ("host", strValue : String) =>
              mongoHost = strValue
            case ("database", strValue : String) =>
              mongoDatabase = strValue
          }
      }
  }
}
