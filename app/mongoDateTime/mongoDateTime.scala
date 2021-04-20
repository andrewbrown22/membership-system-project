package mongoDateTime

import java.time.LocalDateTime

import java.time._
import play.api.libs.json._

trait mongoDateTime {

  implicit val localDateTimeReads: Reads[LocalDateTime] = {
    (__ \ "$date").read[Long].map {
      seconds => LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds), ZoneOffset.UTC)
    }

  }
  //look into dateTime.atZone(ZoneOffset) possibly needs changing due to summertime change
  implicit val localDateTimeWrites: OWrites[LocalDateTime] =
    (dateTime: LocalDateTime) => Json.obj("$date" -> dateTime.atZone(ZoneOffset.ofHours(1)).toInstant.toEpochMilli)
}
