import akka.actor.ActorSystem
import services.UserService
import services.UserService.PostUser

import scala.util.Random

object ApplicationBootstrap extends App {
  implicit val system = ActorSystem("akka-crm")

  implicit val executionContext = system.dispatcher


  val userService = system.actorOf(UserService.props, "userService")

  (1 to 50) foreach { i =>
    userService ! PostUser(Random.nextString(10))
    Thread.sleep(100)
  }
}
