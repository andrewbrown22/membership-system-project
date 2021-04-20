package models

import java.time.LocalDateTime

import mongoDateTime.mongoDateTime
import play.api.libs.json.{Json, OFormat}

case class Session(employeeID: String, lastUpdated: LocalDateTime)

object Session extends mongoDateTime {
  implicit val format: OFormat[Session] = Json.format
}
