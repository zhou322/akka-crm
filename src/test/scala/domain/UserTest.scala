package domain

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import domain.User.{CreateUser, UserCreated}
import domain.UserTest._
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

class UserTest extends TestKit(ActorSystem("UserTest")) with WordSpecLike with MustMatchers with BeforeAndAfterAll with ImplicitSender {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "UserActor" should {
    "add user" in {
      val actor = system.actorOf(TestActors.echoActorProps)
      actor ! CreateUser(userId, userEmail)
      expectMsg(UserCreated(userId, userEmail))
    }
  }
}

object UserTest {
  val userId = 123L
  val userEmail = "user@example.com"
}
