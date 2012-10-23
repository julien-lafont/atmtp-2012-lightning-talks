package service.auth.providers

import play.api._
import play.api.mvc._
import service.auth._

object LinkedIn extends OAuthProvider {

  override val name = "linkedin"
  override val namespace = "li"
  override val permissions = Seq(
    "r_basicprofile", // Get User details
    "r_emailaddress", // Get User email
    "rw_nus")         // Get Network activities
  override val permissionsSep = "+"

  // Override fetch method : define json format by default
  override def fetch(url: String)(implicit request: RequestHeader) =
    super.fetch(url).withHeaders("x-li-format" -> "json")

}

