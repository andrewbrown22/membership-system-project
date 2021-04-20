package controllers

import java.time.LocalDateTime

import javax.inject.Inject
import models.{Card, Employee, Session}
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import repositories._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class EmployeeController @Inject()(cc: ControllerComponents,
                                   employeeRepo: EmployeeRepo,
                                   sessionRepo: SessionRepo)(implicit val ec: ExecutionContext) extends AbstractController(cc){


  def registerEmployee: Action[JsValue] = Action.async(parse.json) {
    implicit request => (for {
      employee <- Future.fromTry(Try {
        request.body.as[Employee]
      })
      _ <- employeeRepo.registerEmployee(employee)
    } yield Ok("Employee succesfully registered")).recoverWith {
      case _ => Future.successful(BadRequest("Incorrect Data"))
    }
  }

  def presentCard(card: Card): Action[AnyContent] = Action.async {
    implicit request =>
      employeeRepo.findEmployee(card).flatMap {
        case Some(employee) =>
          sessionRepo.findValidSession(card).flatMap {
            case Some(_) =>
              sessionRepo.deleteValidSession(card).map(_ => Ok(s"Goodbye ${employee.name}"))
            case None =>
              sessionRepo.createSession(Session(card.employeeID, LocalDateTime.now)).map(_ => Ok(s"Welcome ${employee.name}"))
          }
      }.recoverWith {
        case _ =>
          Future.successful(BadRequest("Incorrect Data"))
      }
  }

  def addFunds(card: Card, amountToAdd: Int): Action[AnyContent] = Action.async {
    employeeRepo.findEmployee(card).flatMap{
      case Some(_) =>
        amountToAdd match {
          case money if money <= 0 => Future.successful(BadRequest("Please enter a positive amount."))
          case _ =>
            employeeRepo.findEmployee(card).flatMap {
            case Some(_) => employeeRepo.addFunds(card, amountToAdd).map{_ => Ok("Your balance has been added.")}
          }
        }
    }
  }
}
