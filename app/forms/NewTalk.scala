package forms

import models.Orateur
import models.Session

case class NewTalk(
  titre: String,
  description: String,
  nom: String,
  bio: String,
  twitter: String)

object NewTalk {
  def enregistrer(talk: NewTalk): Option[Session] = {
    val orateur = Orateur(
      nom = talk.nom,
      bio = talk.bio,
      socialId = talk.twitter,
      socialType = "twitter",
      twitter = Some(talk.twitter))

    Orateur.insert(orateur).flatMap { orateurId =>
      val session = Session(
        titre = talk.titre,
        slug = service.StringUtil.slugify(talk.titre),
        description = talk.description,
        orateurId = orateurId)

      Session.insert(session).flatMap { sessionId =>
        Session.findById(sessionId)
      }
    }
  }
}