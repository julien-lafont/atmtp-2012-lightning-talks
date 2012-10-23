package service.security

import play.api.mvc._
import service.auth.User

object SecuredAction extends Results {
  
  // TODO : Persist user informations in DB
  case class AuthenticatedRequest[A](
    val user: User, request: Request[A]
  ) extends WrappedRequest(request)
  
  def Authenticated[A](p: BodyParser[A])(f: AuthenticatedRequest[A] => Result) = {
    Action(p) { request =>
      request.session.get("provider").flatMap(u => User.fromSession(request.session)).map { user =>
        f(AuthenticatedRequest(user, request))
      }.getOrElse(Redirect(controllers.routes.Login.connect))
    }
  }
  
  // Overloaded method to use the default body parser
  import play.api.mvc.BodyParsers._
  def Authenticated(f: AuthenticatedRequest[AnyContent] => Result): Action[AnyContent]  = {
    Authenticated(parse.anyContent)(f)
  }
  
}