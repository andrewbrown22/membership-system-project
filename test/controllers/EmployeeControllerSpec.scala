package controllers

import java.time.LocalDateTime

import models.{Card, Employee, Session}
import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Matchers._
import org.mockito.Mockito.when
import org.scalatest.OptionValues._
import play.api.libs.json.{JsResultException, JsValue, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import play.api.inject._
import repositories.{EmployeeRepo, SessionRepo}
import reactivemongo.api.commands.UpdateWriteResult
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{AnyContentAsEmpty, Result}

import scala.concurrent.Future

class EmployeeControllerSpec extends WordSpec with MustMatchers with MockitoSugar {

  val mockEmployeeRepo = mock[EmployeeRepo]
  val mockSessionRepo = mock[SessionRepo]

  val validEmployeeId: String = "r7jTG7dqBy5wGO4L"
  val validCard: Card = Card(validEmployeeId)

  val testEmployee: Employee = Employee((validCard),"testName","testEmail","01234567890", 1234, 50.00)

  lazy val builder: GuiceApplicationBuilder =
    new GuiceApplicationBuilder().overrides(
      bind[EmployeeRepo].toInstance(mockEmployeeRepo),
      bind[SessionRepo].toInstance(mockSessionRepo)
    )

  "registerEmployee" must {
    "return an Ok when given the correct data" in {
      //UpdateWriteResult is updating 1 employee in mongo
      when(mockEmployeeRepo.registerEmployee(any()))
        .thenReturn(Future.successful(UpdateWriteResult(true, 1, 1, Seq.empty, Seq.empty, None, None, None)))

      val app: Application = builder.build()

      val employeeAsJson = Json.toJson(Employee(validCard,"testName","testEmail","01234567890", 1234, 50.00))

      val request: FakeRequest[JsValue] = FakeRequest(POST, routes.EmployeeController.registerEmployee.url).withBody(employeeAsJson)

      val result = route(app, request).value

      status(result) mustBe OK
      contentAsString(result) mustBe "Employee succesfully registered"

      app.stop
    }

    "return a BAD_REQUEST when given incorrect data" in {
      when(mockEmployeeRepo.registerEmployee(any()))
        .thenReturn(Future.successful(UpdateWriteResult(true, 1, 1, Seq.empty, Seq.empty, None, None, None)))

      val app: Application = builder.build()

      val employeeAsJson = Json.toJson("invalid Json")

      val request: FakeRequest[JsValue] = FakeRequest(POST, routes.EmployeeController.registerEmployee.url).withBody(employeeAsJson)

      val result = route(app, request).value

      status(result) mustBe BAD_REQUEST

      app.stop
    }
  }

  "presentCard" must {
    "return an Ok response" in {
      when(mockEmployeeRepo.findEmployee(any())).thenReturn(Future.successful(Some(Employee(validCard,"testName","testEmail","01234567890", 1234, 50.00))))

      when(mockSessionRepo.findValidSession(any()))
        .thenReturn(Future.successful(None))

      when(mockSessionRepo.createSession(any()))
        .thenReturn(Future.successful(UpdateWriteResult.apply(ok = true, 1, 1, Seq.empty, Seq.empty, None, None, None)))

      val app: Application = builder.build()

      val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(GET, routes.EmployeeController.presentCard(Card("r7jTG7dqBy5wGO4L")).url)

      val result = route(app, request).value

      status(result) mustBe OK
      contentAsString(result) mustBe "Welcome testName"

      app.stop
    }
    "return a Bad_Response when given incorrect data" in {
      when(mockEmployeeRepo.findEmployee(any())).thenReturn(Future.failed(JsResultException(Seq.empty)))

      val app: Application = builder.build()

      val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(GET, routes.EmployeeController.presentCard(Card("r7jTG7dqBy5wGO4L")).url)

      val result: Future[Result] = route(app, request).value

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe "Incorrect Data"

      app.stop
    }
    "return an Ok with a goodbye message when using presentCard when a session is already valid" in {
      when(mockEmployeeRepo.findEmployee(any())).thenReturn(Future.successful(Some(Employee(validCard,"testName","testEmail","01234567890", 1234, 50.00))))

      when(mockSessionRepo.findValidSession(any()))
        .thenReturn(Future.successful(Some(Session("r7jTG7dqBy5wGO4L", LocalDateTime.now))))

      when(mockSessionRepo.deleteValidSession(any()))
        .thenReturn(Future.successful(UpdateWriteResult.apply(ok = true, 1, 1, Seq.empty, Seq.empty, None, None, None)))

      val app: Application = builder.build()

      val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(GET, routes.EmployeeController.presentCard(Card("r7jTG7dqBy5wGO4L")).url)

      val result = route(app, request).value

      status(result) mustBe OK
      contentAsString(result) mustBe "Goodbye testName"
    }
  }
  "addFunds" in {
    when(mockEmployeeRepo.addFunds(any, any)).thenReturn(Future.successful(Some(Employee(validCard,"testName","testEmail","01234567890", 1234, 50.00))))

    when(mockEmployeeRepo.findEmployee(any)).thenReturn(Future.successful(Some(Employee(validCard,"testName","testEmail","01234567890", 1234, 50.00))))

    val app: Application = builder.build()

    val request: FakeRequest[AnyContentAsEmpty.type] = FakeRequest(POST, routes.EmployeeController.addFunds(Card("r7jTG7dqBy5wGO4L"), 100.00).url)

    val result = route(app, request).value

    status(result) mustBe OK
  }

}
