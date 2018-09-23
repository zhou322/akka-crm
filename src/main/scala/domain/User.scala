package domain

import akka.actor.Props
import akka.http.scaladsl.model.DateTime
import akka.persistence.SnapshotMetadata


object User {
  import AggregateRoot._

  final case class UserState(id: Long, email: String, createdAt: DateTime, updatedAt: DateTime) extends State

  // command and event
  case class CreateUser(id: Long, email: String) extends Command
  case class UserCreated(id: Long, email: String) extends Event

  case class AddUserDetail(id: Long, firstName: Option[String], lastName: Option[String]) extends Command
  case class UserDetailAdded(id: Long, firstName: Option[String], lastName: Option[String]) extends Event

  case class DeleteUser(id: Long)
  case class UserDeleted(id: Long)

  def props(id: Long): Props = Props(new User(id))
}

class User(id: Long) extends AggregateRoot {
  import User._

  override def persistenceId: String = id.toString

  override def receive: Receive = ???

  override def receiveCommand: Receive = ???

  override def updateState(event: AggregateRoot.Event): Unit = {
    case UserCreated(userId, email) ⇒
      state = UserState()
    case UserDetailAdded(userId, firstName, lastName) ⇒ ???
    case UserDeleted(userId) ⇒ ???
  }

  override protected def restoreFromSnapshot(metaData: SnapshotMetadata, state: AggregateRoot.State): Unit = ???
}
