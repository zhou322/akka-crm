package services

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import domain.AggregateRoot

object ServiceActor {
  val maxAggregateActor = 50
  val maxToKillAtOnce = 20

  final case class PendingCommand(aggregateActor: ActorRef, aggregateId: String, command: AggregateRoot.Command)
}

trait ServiceActor extends Actor with ActorLogging {
  import ServiceActor._

  private var aggregateActorBeingKilled: Set[ActorRef] = Set.empty
  private var pendingAggregateCommand: Vector[AggregateRoot.Command] = Vector.empty

  def processAggregateCommand(aggregateId: String, aggregateCommand: AggregateRoot.Command): Unit = {

    val maybeAggregateActor = context.child(aggregateId)

    maybeAggregateActor match {
      case Some(aggregateActor) ⇒ if (aggregateActorBeingKilled.contains(aggregateActor)) {
        log.debug(s"[ServiceActor] add command to pending command for aggreagte item $aggregateId")
        pendingAggregateCommand :+ PendingCommand(aggregateActor, aggregateId, aggregateCommand)
      }

      case Some(aggregateActor) ⇒ aggregateActor forward aggregateCommand

      case None ⇒ create(aggregateId) forward aggregateCommand
    }
  }

  def aggregateProps(id: String): Props

  protected def create(id: String): ActorRef = {
    val agg = context.actorOf(aggregateProps(id), id)
    context watch agg
    agg
  }
}
