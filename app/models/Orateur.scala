package models

import play.api.Play.current
import java.util.Date
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.dao._
import com.mongodb.casbah.Imports._
import se.radley.plugin.salat._
import mongoContext._

case class Orateur(
	@Key("_id") id: ObjectId = new ObjectId,
	nom: String,
	bio: String,
	socialId: String, // twitter name, google+ id ...
	socialType: String, // twitter|google|facebook ...
	avatar: Option[String] = None,
	twitter: Option[String] = None)

object Orateur extends ModelCompanion[Orateur, ObjectId] { 
  val dao = new SalatDAO[Orateur, ObjectId](collection = mongoCollection("orateurs")) { }
  
  def findById(id: ObjectId) : Option[Orateur] = dao.findOneById(id)
  
  def insertTestData(nom: String, bio: String) = 
  	dao.insert(Orateur(nom = nom, bio = bio, socialId = "/", socialType = "/"))
  
}