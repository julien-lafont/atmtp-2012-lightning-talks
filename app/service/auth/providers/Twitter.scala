package service.auth.providers

import play.api._
import play.api.mvc._
import service.auth._

class Twitter extends OAuthProvider {

  override val name = "twitter"
  override val namespace = "tw"

  override def getUser(implicit req: RequestHeader) = {
    fetch("https://api.twitter.com/1.1/account/verify_credentials.json").get().await(10000).fold(
      onError => {
        Logger.error("timed out waiting for Twitter")
        None
      },
      response =>
      {
        try {
          val me = response.json
          val id = (me \ "id").as[Int]
          val username = (me \ "screen_name").as[String]
          val name = (me \ "name").as[String]
          val description = (me \ "description").as[String]
          val profileImage = (me \ "profile_image_url").asOpt[String]
          Some(User(username, name, this.name, description, profileImage))
        } catch {
          case _ => {
            Logger.error("Error during fetching user details")
            None
          }
        }
        
      }
    )
    
  }
  
}
