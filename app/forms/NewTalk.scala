package forms

import models.Orateur
import models.Session

case class NewTalk(
  titre: String,
  description: String,
  nom: String,
  bio: String,
  socialId: String,
  socialType: String)

object NewTalk {
  def enregistrer(talk: NewTalk): Option[Session] = {
    val orateur = Orateur(
      nom = talk.nom,
      bio = talk.bio,
      socialId = talk.socialId,
      socialType = talk.socialType)

    Orateur.insert(orateur).flatMap { orateurId =>
      val session = Session(
        titre = talk.titre,
        slug = Session.slugify(talk.titre),
        description = talk.description,
        orateurId = orateurId)

      Session.insert(session).flatMap { sessionId =>
        Session.findById(sessionId)
      }
    }
  }
}