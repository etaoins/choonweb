package local.choonweb.musicscan
import scala.util.parsing.json.JSON
import scala.io.Source
import java.io.InputStream

class Configuration(config : Source) {
  sealed class ConfigParseException extends Exception;
  final case class UnknownKeyException(key : String) extends ConfigParseException;
  final class UnexpectedTypeException extends ConfigParseException;

  var mongoHost = "localhost"
  var mongoDatabase = "choonweb"
  var musicPrefix = "http://localhost:8080/"

  for(fullJson <- JSON.parseFull(config.mkString)) fullJson match {
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
            case _ => throw new UnknownKeyException(mongoPair._1)
          }
        case _ => throw new UnknownKeyException(pair._1)
      }
    case _ => throw new UnexpectedTypeException
  }
}
