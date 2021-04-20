package models

import models.Card.pathBindable
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.Json

class CardSpec extends WordSpec with MustMatchers {

  val validEmployeeId: String = "r7jTG7dqBy5wGO4L"
  val invalidEmployeeId: String = "r7j"

  "Card model" must {
    "read" in {
      val testId = Card(validEmployeeId)
      val expectedJson = Json.obj("employeeID" -> validEmployeeId)

      Json.toJson(testId) mustEqual expectedJson
    }
    "write" in {
      val testId = Card(validEmployeeId)
      val expectedJson = Json.obj("employeeID" -> validEmployeeId)

      expectedJson.as[Card] mustEqual testId
    }

    "return 'EmployeeID is not valid' if employeeID is not 16 characters long" in {
      val invalidTestID = Card(invalidEmployeeId)
      val result = "Employee ID is not valid."

      pathBindable.bind("", invalidTestID.toString) mustBe Left(result)
    }
  }


}
