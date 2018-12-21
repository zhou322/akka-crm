package services

import akka.actor.Props
import services.UserService.PostUser

object UserService {
  def props: Props = Props(new UserService())

  final case class PostUser(email: String)

}

class UserService extends ServiceActor {
  import domain.User._
  import domain.User
  override def aggregateProps(id: String): Props = User.props(id)

  override def processCommand: Receive = {
    case PostUser(email) ⇒
      val id = System.currentTimeMillis()
      processAggregateCommand(id.toString, CreateUser(id.toString, email))
  }

}
