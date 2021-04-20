package models

import java.time.LocalDateTime

import org.scalatest.{MustMatchers, WordSpec}
import mongoDateTime.mongoDateTime
import play.api.libs.json.Json

class SessionSpec extends WordSpec with MustMatchers with mongoDateTime {

  "The Session model" must {
    val employeeID = "r7jTG7dqBy5wGO4L"

    "write" in {

      val currentTime = LocalDateTime.now
      val session: Session = Session(employeeID, currentTime)

      val expected = Json.obj("employeeID" -> employeeID, "lastUpdated" -> currentTime)

    Json.toJson(session) mustEqual expected
    }
  }

}
