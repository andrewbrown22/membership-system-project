package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Employee(employeeId: Card, name: String, email: String, mobileNumber: String, pin: Int, availableBalance: Int)

object Employee {

  implicit val reads: Reads[Employee] = (__.read[Card] and
    (__ \ "name").read[String] and
    (__ \ "email").read[String] and
    (__ \ "mobileNumber").read[String] and
    (__ \ "pin").read[Int] and
    (__ \ "availableBalance").read[Int]) (Employee.apply _)


  implicit val writes: OWrites[Employee] = (__.write[Card] and
    (__ \ "name").write[String] and
    (__ \ "email").write[String] and
    (__ \ "mobileNumber").write[String] and
    (__ \ "pin").write[Int] and
    (__ \ "availableBalance").write[Int]) (unlift(Employee.unapply))
}
