import play.api._

import models._
import anorm._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    InitialData.insert()
  }

}

object InitialData {

  def insert() = {

    if (Session.findAll().isEmpty) {

      val orateur1 = Orateur.insertTestData("Julien Lafont", "Architecte web")
      val orateur2 = Orateur.insertTestData("Kev Adams", "Scrum master depuis 1906")
      val orateur3 = Orateur.insertTestData("Charlie Parker", "Consultant")

      val session1 = Session.insertTestData("Le waterfall ou rien", "blabla", orateur1.get)
      val session2 = Session.insertTestData("Le cycle en V m'a tuer", "blabla2", orateur2.get)
      val session3 = Session.insertTestData("To Scrum or not to Scrum", "blabla3", orateur3.get)
      val session4 = Session.insertTestData("Scrum, mon amour, ma haine", "blabla4", orateur1.get)

    }

  }

}