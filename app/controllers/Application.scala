
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
import play.api.mvc.Results.EmptyContent

object Application extends Controller {
  
  private val expireTime = 3456000

  def index = Action { implicit req =>
    Ok(views.html.index(Session.findAllTalks(getTriSession), User(req.session)))
  }

  def voter(slug: String) = Action { implicit req =>
    Session.findBySlug(slug).map { sess =>
      val id = sess.id.toString

      if (Vote.peutVoter(sess)) {
        Session.ajouterVote(sess)
        
        val cookie = req.cookies.get("vote").getOrElse(Cookie("vote", "", expireTime))
        val cookieAvecVote = cookie.copy(value = cookie.value.split("-").+:(id).mkString("-"), maxAge = expireTime)
        redirigerApresVote(slug).withCookies(cookieAvecVote)
      } else {
        Session.retirerVote(sess)
        val cookie = req.cookies.get("vote").getOrElse(Cookie("vote", "", expireTime))
        val cookieSansVote = cookie.copy(value = cookie.value.split("-").filter(_ != id).mkString("-"), maxAge = expireTime)
        redirigerApresVote(slug).withCookies(cookieSansVote)
      }

    }.getOrElse(NotFound)
  }

  private def redirigerApresVote(slug: String)(implicit req : RequestHeader) : SimpleResult[EmptyContent] = {
    if (req.queryString.get("redir").isDefined) {
      Redirect(routes.Application.index)
    } else {
      Redirect(routes.Application.detail(slug))
    }
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
