package controllers

import play.api.mvc._
import service.auth.providers.Twitter
import play.api.PlayException
import service.auth.ProviderDispatching

object Login extends Controller {

  def connect = Action {
    Ok(views.html.connect())
  }

  def authenticate(provider: String) = Action { implicit request =>
    val redirOk = routes.Login.fetchUserInformations
    ProviderDispatching.get("twitter").auth(redirOk)
  }

  def fetchUserInformations() = Action { implicit request =>
    ProviderDispatching.get(request.session("provider")).getUser.map { user =>
      println("Authentification : "+user)
      Redirect(routes.Propositions.formulaire).withSession(
          request.session 
          + ("user-username" -> user.username)
          + ("user-avatar" -> user.avatar.getOrElse(""))
          + ("user-description" -> user.description)
          + ("user-name" -> user.name))
      
    }.getOrElse{
      Unauthorized("Une erreur est survenue durant le processus d'authentification")
    }
  }

}