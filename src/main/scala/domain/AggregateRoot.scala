package domain

import akka.actor.ActorLogging
import akka.persistence.{AtLeastOnceDelivery, PersistentActor, SnapshotMetadata, SnapshotOffer}
import common.Acknowledge

object AggregateRoot {
  trait Command
  trait Event

  trait State

  val eventsPerSnapshot = 5
}

trait AggregateRoot extends PersistentActor with AtLeastOnceDelivery with ActorLogging {
  import AggregateRoot._

  private var eventsSinceLastSnapshot: Int = 0
  private var state: State = _

  def updateState(event: Event): Unit

  val receiveRecover: Receive = {
    case event: Event                          ⇒ updateState(event)
    case SnapshotOffer(metaData, state: State) ⇒ restoreFromSnapshot(metaData, state)
  }

  protected def restoreFromSnapshot(metaData: SnapshotMetadata, state: State): Unit =
    this.state = state

  protected def afterEventPersisted(evt: Event): Unit = {
    eventsSinceLastSnapshot += 1
    if (eventsSinceLastSnapshot >= eventsPerSnapshot) {
      log.debug("{} events reached, saving snapshot", eventsPerSnapshot)
      saveSnapshot(state)
      eventsSinceLastSnapshot = 0
    }
    publish(evt)
    updateAndRespond(evt)
  }


  private def updateAndRespond(evt: Event): Unit = {
    updateState(evt)
    respond()
  }

  protected def respond(): Unit = {
    context.parent ! Acknowledge(persistenceId)
  }

  private def publish(event: Event): Unit =
    context.system.eventStream.publish(event)

}
