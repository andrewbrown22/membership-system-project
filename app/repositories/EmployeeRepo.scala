package repositories

import javax.inject.Inject
import models.{Card, Employee}
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.{FindAndModifyCommand, WriteResult}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json.ImplicitBSONHandlers.JsObjectDocumentWriter
import reactivemongo.api.WriteConcern

import scala.concurrent.{ExecutionContext, Future}



class EmployeeRepo @Inject()(mongoDB: ReactiveMongoApi,
                             cc: ControllerComponents)(implicit val ec: ExecutionContext) extends AbstractController(cc) {

  val databaseCollection: Future[JSONCollection] = {
    mongoDB.database.map(_.collection[JSONCollection]("employees"))
  }

  def update(collection: JSONCollection, identifier: JsObject, updater: JsObject): Future[FindAndModifyCommand.Result[collection.pack.type]] = {
    collection.findAndUpdate(selector = identifier,
      update = updater,
      fetchNewObject = true,
      false,
      None,
      None,
      false,
      writeConcern = WriteConcern.Default,
      None,
      None,
      Seq.empty)
  }

  def registerEmployee(newEmployee: Employee): Future[WriteResult] = {
    databaseCollection.flatMap(_.insert.one(newEmployee))
  }

  def findEmployee(card: Card): Future[Option[Employee]] = {
    databaseCollection.flatMap(_.find(Json.obj("employeeID" -> card.employeeID), None).one[Employee])
  }

  def addFunds(card: Card, balance: Int): Future[Option[Employee]] = {
    databaseCollection.flatMap {
      x =>
        val identifier = Json.obj("employeeID" -> card.employeeID)
        val updater = Json.obj("$inc" -> Json.obj("availableBalance" -> balance))
        update(x, identifier, updater).map(_.result[Employee])
    }
  }

}
