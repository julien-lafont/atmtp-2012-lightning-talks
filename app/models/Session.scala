package models

import play.api.Play.current
import mongoContext._
import com.novus.salat._
import com.novus.salat.annotations._
import se.radley.plugin.salat._
import com.mongodb.casbah.Imports.{ MongoDBObject => $, _ }
import com.novus.salat.dao.{ SalatDAO, ModelCompanion }
import java.util.Date
import play.api.mvc.RequestHeader
import service.StringUtil
import scala.util.Random

case class Session(
  @Key("_id") id: ObjectId = new ObjectId,
  @Key("orateur_id") orateurId: ObjectId,
  titre: String,
  slug: String,
  description: String,
  date: Date = new Date(),
  vote: Int = 0,
  consultation: Int = 0) {

  def ajouterVote() =
    this.copy(vote = vote + 1)

  def ajouterConsultation() =
    this.copy(consultation = consultation + 1)

  def withDetails(implicit request: RequestHeader) : Talk =
    Talk(this, Orateur.findById(orateurId).get, Vote.peutVoter(this)(request))
    
  def nbVotesHtml() : String = vote + (if (vote <= 1) " vote" else " votes")
    
}

case class Talk(
  session: Session,
  orateur: Orateur,
  statutVote: Boolean = false)

object Session extends ModelCompanion[Session, ObjectId] {
  val dao = new SalatDAO[Session, ObjectId](collection = mongoCollection("sessions")) {}

  def findById(id: ObjectId): Option[Session] =
    dao.findOneById(id)

  def findBySlug(slug: String): Option[Session] =
    dao.findOne($("slug" -> slug))

  def findAll(tri: Option[(String, Int)] = Some(("date", -1))): List[Session] = {
    val finder = dao.find($.empty)
    if (tri.isDefined) finder.sort(orderBy = $(tri.get._1 -> tri.get._2)).toList
    else new Random().shuffle(finder.toList)
  }

  def findAllTalks(tri: Option[(String, Int)] = Some(("date", -1)))(implicit request: RequestHeader): List[Talk] =
    findAll(tri).map(_.withDetails).toList

  def ajouterVote(session: Session)(implicit req: RequestHeader) = {
    dao.save(session.copy(vote = session.vote + 1))
    //Vote.historiser(session)
  }
  
  def retirerVote(session: Session)(implicit req: RequestHeader) = {
    dao.save(session.copy(vote = session.vote - 1))
    //Vote.supprimer(session)
  }

  def consulter(session: Session): Session = {
    val sessionConsultee = session.ajouterConsultation()
    dao.save(sessionConsultee)
    sessionConsultee
  }

  def insertTestData(titre: String, description: String, orateur: ObjectId): Option[ObjectId] =
    dao.insert(Session(titre = titre, slug = service.StringUtil.slugify(titre), description = description, orateurId = orateur))
    

}