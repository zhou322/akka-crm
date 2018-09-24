import akka.actor.ActorSystem

object ApplicationBootstrap extends App {
  implicit val system = ActorSystem("akka-crm")

  implicit val executionContext = system.dispatcher
}
