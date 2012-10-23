package service.auth

import play.api.mvc._
import play.api.libs.ws.WS.WSRequestHolder

trait GenericProvider extends Results {

  // Generic provider settings (override it)
  def name: String
  def namespace: String

  def permissions: Seq[String] = Seq.empty
  def permissionsSep = ","

  // Common config
  lazy val authRoute: Call = controllers.routes.Login.authenticate(name)

  // Basic operations on providers
  def auth(redirectRoute: Call)(implicit request: RequestHeader): Result
  def getToken(implicit request: RequestHeader): Any // Put RequestToken here
  def fetch(url: String)(implicit request: RequestHeader): WSRequestHolder
  def getUser(implicit req: RequestHeader) : Option[User] = None  

}