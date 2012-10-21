package controllers

import play.api.mvc._
import models.Orateur
import models.Session
import models.Vote
import forms.NewTalk
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object Propositions extends Controller {

  def formulaire = Action {
    Ok(views.html.creer(talkForm))
  }

  def enregistrer = Action { implicit req =>
    talkForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.creer(formWithErrors)),
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
      "twitter" -> text.verifying(nonEmpty))
      (NewTalk.apply)(NewTalk.unapply))

}