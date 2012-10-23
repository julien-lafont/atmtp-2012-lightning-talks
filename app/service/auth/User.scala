package service.auth

case class User(
  username: String,
  name: String,
  socialType: String,
  description: String,
  avatar: Option[String] = None)

object User {
  def fromSession(sess: play.api.mvc.Session): Option[User] = {
    for (
      login <- sess.get("user-username");
      social <- sess.get("provider");
      name <- sess.get("user-name");
      desc <- sess.get("user-description")
    ) yield User(login, name, social, desc, sess.get("user-description"))
  }
}