package domain

import akka.actor.ActorLogging
import akka.persistence.{PersistentActor, SnapshotMetadata, SnapshotOffer}

object AggregateRoot {
  trait Command
  trait Event

  trait State

  val eventsPerSnapshot = 20
}

trait AggregateRoot extends PersistentActor with ActorLogging {
  import AggregateRoot._

  protected var state: State = _

  def updateState(event: Event): Unit

  val receiveRecover: Receive = {
    case event: Event                          ⇒ updateState(event)
    case SnapshotOffer(metaData, state: State) ⇒ restoreFromSnapshot(metaData, state)
  }

  protected def restoreFromSnapshot(metaData: SnapshotMetadata, state: State)
}
