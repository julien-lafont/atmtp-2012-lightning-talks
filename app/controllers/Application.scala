
package controllers

import play.api.mvc._
import models.Orateur
import models.Session
import models.Vote
import forms.NewTalk
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._


object Application extends Controller {
 
  def index = Action { implicit req =>
  	Ok(views.html.index(Session.findAllTalks(getTriSession)))
  }
  
  def soumettre = Action {
    Ok(views.html.creer(talkForm))
  }
  
  def voter(slug: String) = Action { implicit req =>
  	Session.findBySlug(slug).map{ session =>
  		if (Vote.peutVoter(session)) {
  	      Session.voter(session)
  	      Redirect(routes.Application.detail(slug))
  		} else Forbidden
  	}.getOrElse(NotFound)
  }

  def detail(slug: String) = Action { implicit req =>
  	Session.findBySlug(slug).map{ session =>
  		Ok(views.html.detail(Session.consulter(session).withDetails))
  	}.getOrElse(NotFound)
  }
  
  def ajouterSession() = Action { implicit req =>
    talkForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.creer(formWithErrors)),
        talk => NewTalk.enregistrer(talk) match {
        	case Some(session) => Redirect(routes.Application.detail(session.slug))
        	case None => BadRequest
        }
    )
  }
  
  def trier(colonne: String) = Action { req =>
  	val sens = if (req.session.get("tri").getOrElse("") == colonne)
  	  req.session.get("sens").map(_.toInt).getOrElse(-1) * (-1)
  	else
  	  1
  	
  	Redirect(routes.Application.index()).withSession(req.session + ("tri" -> colonne) + ("sens" -> sens.toString))
  }
  
  private val talkForm = Form(
    mapping(
      "titre" -> text.verifying(nonEmpty),
      "description" -> text.verifying(minLength(50)),
      "nom" -> text.verifying(nonEmpty),
      "bio" -> text,
      "socialId" -> text.verifying(nonEmpty),
      "socialType" -> text.verifying(nonEmpty)
  )(NewTalk.apply)(NewTalk.unapply))
  
  private def getTriSession(implicit request: RequestHeader) = {
  	val colonne = request.session.get("tri")
  	val sens = request.session.get("sens")
  	
  	if (colonne.isDefined && sens.isDefined) Some(colonne.get, sens.get.toInt)
  	else None
  }
  
}
