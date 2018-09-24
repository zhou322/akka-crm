package domain

import akka.actor.Props
import akka.http.scaladsl.model.DateTime

object User {
  import AggregateRoot._

  final case class UserDetail(firstName: Option[String], lastName: Option[String])
  final case class UserState(id: Long, email: String, detail: Option[UserDetail],
                             createdAt: DateTime, updatedAt: DateTime) extends State
  // commands and events
  case class CreateUser(id: Long, email: String) extends Command
  case class UserCreated(id: Long, email: String) extends Event

  case class AddUserDetail(id: Long, firstName: Option[String], lastName: Option[String]) extends Command
  case class UserDetailAdded(id: Long, firstName: Option[String], lastName: Option[String]) extends Event

  case class ChangeUserDetail(id: Long, firstName: Option[String], lastName: Option[String]) extends Command
  case class UserDetailChanged(id: Long, firstName: Option[String], lastName: Option[String]) extends Event

  case class DeleteUser(id: Long)
  case class UserDeleted(id: Long)

  def props(id: Long): Props = Props(new User(id))
}

class User(id: Long) extends AggregateRoot {
  import User._

  protected var state: UserState = _

  override def persistenceId: String = id.toString

  override def receiveCommand: Receive = {
    case CreateUser(userId, email) ⇒
      persist(UserCreated(userId, email))(afterEventPersisted)
    case AddUserDetail(userId, firstName, lastName) ⇒ persist(UserDetailAdded(userId, firstName, lastName))(afterEventPersisted)
    case ChangeUserDetail(userId, firstName, lastName) ⇒ persist(UserDetailAdded(userId, firstName, lastName))(afterEventPersisted)
  }

  override def updateState(event: AggregateRoot.Event): Unit = event match {
    case UserCreated(userId, email) ⇒
      state = UserState(userId, email, None, DateTime.now, DateTime.now)
    case UserDetailAdded(userId, firstName, lastName) ⇒
      state = state.copy(detail = Some(UserDetail(firstName, lastName)))
    case UserDetailChanged(userId, firstName, lastName) ⇒
      state = state.copy(detail = Some(UserDetail(firstName, lastName)))
  }
}
