import akka.actor.ActorSystem
import domain.User
import domain.User.CreateUser

object ApplicationBootstrap extends App {
  implicit val system = ActorSystem("akka-crm")

  implicit val executionContext = system.dispatcher


  val userActor = system.actorOf(User.props(1L))

  userActor ! CreateUser(1L, "example.com")

}
