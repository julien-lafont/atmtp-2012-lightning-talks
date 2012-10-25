
package controllers

import play.api.mvc._
import models.Orateur
import models.Session
import models.Vote
import forms.NewTalk
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import service.auth.providers.Twitter
import service.auth.User

object Application extends Controller {

  def index = Action { implicit req =>
    Ok(views.html.index(Session.findAllTalks(getTriSession), User(req.session)))
  }

  def voter(slug: String) = Action { implicit req =>
    Session.findBySlug(slug).map { session =>
      if (Vote.peutVoter(session)) {
        Session.ajouterVote(session)
      } else {
        Session.retirerVote(session)
      }
      
      if (req.queryString.get("redir").isDefined) {
        Redirect(routes.Application.index)
      } else {
        Redirect(routes.Application.detail(slug))
      }
    }.getOrElse(NotFound)
  }

  def detail(slug: String) = Action { implicit req =>
    Session.findBySlug(slug).map { session =>
      Ok(views.html.detail(Session.consulter(session).withDetails, User(req.session)))
    }.getOrElse(NotFound)
  }

  def trier(colonne: String) = Action { req =>
    val sens = if (req.session.get("tri").getOrElse("") == colonne)
      req.session.get("sens").map(_.toInt).getOrElse(-1) * (-1)
    else
      1

    Redirect(routes.Application.index()).withSession(req.session + ("tri" -> colonne) + ("sens" -> sens.toString))
  }

  private def getTriSession(implicit request: RequestHeader) = {
    val colonne = request.session.get("tri")
    val sens = request.session.get("sens")

    if (colonne.isDefined && sens.isDefined) Some(colonne.get, sens.get.toInt)
    else None
  }

}
