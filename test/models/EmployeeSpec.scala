package models

import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.{JsObject, Json}

class EmployeeSpec extends WordSpec with MustMatchers{

  val validEmployeeId: Card = Card("r7jTG7dqBy5wGO4L")

  "Employee" must {
    "read" in {
      val expectedJson: JsObject = Json.obj("employeeID" -> "r7jTG7dqBy5wGO4L",
        "name" -> "testName",
        "email" -> "testEmail@hotmail.com",
        "mobileNumber" -> "01234567890",
        "pin" -> 1234,
        "availableBalance" -> 50.00)

      val expectedEmployee: Employee = Employee(validEmployeeId, "testName", "testEmail@hotmail.com", "01234567890", 1234, 50.00)

      expectedJson.as[Employee] mustEqual expectedEmployee
    }
    "write" in {
      val expectedJson: JsObject = Json.obj("employeeID" -> "r7jTG7dqBy5wGO4L",
        "name" -> "testName",
        "email" -> "testEmail@hotmail.com",
        "mobileNumber" -> "01234567890",
        "pin" -> 1234,
        "availableBalance" -> 50.00)

      val expectedEmployee: Employee = Employee(validEmployeeId, "testName", "testEmail@hotmail.com", "01234567890", 1234, 50.00)

      Json.toJson(expectedEmployee) mustEqual expectedJson
    }
  }

}
