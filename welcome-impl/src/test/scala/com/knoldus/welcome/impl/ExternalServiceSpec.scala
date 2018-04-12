package com.knoldus.welcome.impl

import akka.NotUsed
import com.knoldus.welcome.api.{UserData, WelcomeService}
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Future

class ExternalServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(ServiceTest.defaultSetup
    .withCassandra(false)) { ctx =>
    new WelcomeApplication(ctx) with LocalServiceLocator {
      lazy val welcomeService = new WelcomeServiceStub()
    }
  }

  val client = server.application.welcomeService

  override protected def afterAll() = server.stop()

  "Welcome service" should {

    "should consume user from external service" in {
      val expectedResult = UserData(1, 1, "body", "title")
      client.consumeUser().invoke().map { response =>
        response should ===(expectedResult)

      }
    }
  }
}

class WelcomeServiceStub extends WelcomeService {

  override def welcome(name: String): ServiceCall[NotUsed, String] = ???

  override def consumeUser() = ServiceCall { _ =>
    val user = UserData(1, 1, "body", "title")
    val result: Future[UserData] = Future.successful(user)
    result
  }
}



