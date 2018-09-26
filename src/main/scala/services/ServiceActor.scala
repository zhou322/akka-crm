package services

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, Props}
import domain.AggregateRoot

object ServiceActor {
  val maxAggregateActor = 50
  val maxToKillAtOnce = 20

  final case class PendingCommand(aggregateActor: ActorRef, aggregateId: String, command: AggregateRoot.Command)
}

trait ServiceActor extends Actor with ActorLogging {
  import ServiceActor._

  def processCommand : Receive

  override def receive: Receive = processCommand

  def processAggregateCommand(aggregateId: String, aggregateCommand: AggregateRoot.Command): Unit = {
    val maybeAggregateActor = context.child(aggregateId)

    maybeAggregateActor match {
      case Some(aggregateActor) ⇒ aggregateActor forward aggregateCommand
      case None ⇒ create(aggregateId) forward aggregateCommand
    }
  }

  def aggregateProps(id: String): Props

  protected def create(id: String): ActorRef = {
    killActorsIfNecessary
    val agg = context.actorOf(aggregateProps(id), id)
    context watch agg
    agg
  }

  private def killActorsIfNecessary(): Unit ={
    val actorCount = context.children.size
    // kill actors if more than 50
    if (actorCount > maxAggregateActor) {
      log.debug(s"[ServiceActor] ${context.getClass} reach the maxiumal limit. killing $maxToKillAtOnce actors now.")

      val actorWillBeKilled = context.children.take(maxToKillAtOnce)

      actorWillBeKilled foreach (_ ! PoisonPill)
    }
  }
}
