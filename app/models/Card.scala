package models

import play.api.libs.json.{OWrites, Reads, __}
import play.api.mvc.PathBindable

case class Card(employeeID: String)

object Card {

  implicit val reads: Reads[Card] = (__ \ "employeeID").read[String].map(Card(_))
  implicit val writes: OWrites[Card] = (__ \ "employeeID").write[String].contramap(_.employeeID)

  implicit val pathBindable: PathBindable[Card] = {
    new PathBindable[Card] {
      override def bind(key: String, value: String): Either[String, Card] =
        if (value.length == 16) Right(Card(value))
        else Left("Employee ID is not valid.")

      override def unbind(key: String, value: Card): String = value.employeeID
    }
  }


}
