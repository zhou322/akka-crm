package domain

import akka.actor.Props
import akka.http.scaladsl.model.DateTime

object User {
  import AggregateRoot._

  final case class UserDetail(firstName: Option[String], lastName: Option[String])
  final case class UserState(id: String, email: String, detail: Option[UserDetail],
                             createdAt: DateTime, updatedAt: DateTime) extends State
  // commands and events
  case class CreateUser(id: String, email: String) extends Command
  case class UserCreated(id: String, email: String) extends Event

  case class AddUserDetail(id: String, firstName: Option[String], lastName: Option[String]) extends Command
  case class UserDetailAdded(id: String, firstName: Option[String], lastName: Option[String]) extends Event

  case class ChangeUserDetail(id: String, firstName: Option[String], lastName: Option[String]) extends Command
  case class UserDetailChanged(id: String, firstName: Option[String], lastName: Option[String]) extends Event

  case class DeleteUser(id: String)
  case class UserDeleted(id: String)

  def props(id: String): Props = Props(new User(id))
}

class User(id: String) extends AggregateRoot {
  import User._

  protected var state: UserState = _

  override def persistenceId: String = s"$aggregateName-$id"

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

  override val aggregateName: String = "User"
}
