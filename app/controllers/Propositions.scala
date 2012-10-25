package controllers

import play.api.mvc._
import models.Orateur
import models.Session
import models.Vote
import forms.NewTalk
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import service.security.SecuredAction
import service.auth.User

object Propositions extends Controller {

  def formulaire = SecuredAction.Authenticated { req =>
    val formPreRempli = talkForm.bind(Map("twitter" -> req.user.username, "nom" -> req.user.name, "bio" -> req.user.description))
      .copy(errors = Seq())
    Ok(views.html.creer(formPreRempli, User(req.session)))
  }

  def enregistrer = SecuredAction.Authenticated { req =>
    talkForm.bindFromRequest()(req.request).fold(
      formWithErrors => BadRequest(views.html.creer(formWithErrors, User(req.session))),
      talk => NewTalk.enregistrer(talk) match {
        case Some(session) => Redirect(routes.Application.detail(session.slug))
        case None          => BadRequest
      })
  }

  private val talkForm = Form(
    mapping(
      "titre" -> text.verifying(nonEmpty),
      "description" -> text.verifying(minLength(50)),
      "nom" -> text.verifying(nonEmpty),
      "bio" -> text.verifying(nonEmpty),
      "twitter" -> text.verifying(nonEmpty))(NewTalk.apply)(NewTalk.unapply))

}