package repositories

import play.api.Configuration
import javax.inject.Inject
import models.{Card, Session}
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json.ImplicitBSONHandlers.JsObjectDocumentWriter
import scala.concurrent.{ExecutionContext, Future}

class SessionRepo @Inject()(mongo: ReactiveMongoApi, config: Configuration)(implicit val ec: ExecutionContext) {

  val sessionCollection: Future[JSONCollection] =
    mongo.database.map(_.collection[JSONCollection]("session"))

  val timeOutLimit = config.get[Int]("session.timeOutLimit")

  private val index: Index = Index(
    key = Seq("lastUpdated" -> IndexType.Ascending),
    name = Some("session-index"),
    options = BSONDocument("expireAfterMinutes" -> timeOutLimit)
  )

  sessionCollection.map(_.indexesManager.ensure(index))

  def createSession(session: Session): Future[WriteResult] =
    sessionCollection.flatMap(_.insert.one(session))


  def findValidSession(card: Card): Future[Option[Session]] =
    sessionCollection.flatMap(_.find(Json.obj("employeeID" -> card.employeeID), None).one[Session])

  def deleteValidSession(card: Card): Future[WriteResult] =
    sessionCollection.flatMap(_.delete.one(Json.obj("employeeID" -> card.employeeID)))

}
