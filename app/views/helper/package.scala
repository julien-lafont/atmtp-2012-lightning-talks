package views.html.helper

import views.html.helper._

package object twitterBootstrapV2 {
	
  implicit val twitterBootstrapV2Field = new FieldConstructor {
    def apply(elts: FieldElements) = twitterBootstrapV2FieldConstructor(elts)
  }

}