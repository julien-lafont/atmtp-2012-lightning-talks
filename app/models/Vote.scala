package models

import play.api.Play.current
import mongoContext._
import com.novus.salat._
import com.novus.salat.annotations._
import se.radley.plugin.salat._
import com.mongodb.casbah.Imports.{ MongoDBObject => $, _ }
import com.novus.salat.dao.{ SalatDAO, ModelCompanion }
import play.api.mvc._

case class Vote(
	@Key("_id") id: ObjectId = new ObjectId,
	@Key("session_id") sessionId: ObjectId,
	ip: String)

object Vote extends ModelCompanion[Vote, ObjectId] {
	val dao = new SalatDAO[Vote, ObjectId](collection = mongoCollection("votes")) {}
	
	def historiser(session: Session)(implicit request: RequestHeader) : Option[ObjectId] = 
		dao.insert(Vote(sessionId = session.id, ip = request.remoteAddress))

	def peutVoter(session: Session)(implicit request: RequestHeader) : Boolean = 
		dao.findOne($("session_id" -> session.id, "ip" -> request.remoteAddress)).isEmpty
}