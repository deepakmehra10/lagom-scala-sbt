package com.knoldus.welcome.impl

import com.knoldus.welcome.api._
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class WelcomeServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(ServiceTest.defaultSetup
    .withCassandra(true)) { ctx =>
    new WelcomeApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[WelcomeService]

  override protected def afterAll() = server.stop()

  "welcome service" should {

    "should greet with custom message" in {
      client.welcome("Deepak").invoke().map { response =>
        response should ===("Welcome, Deepak")

      }
    }
  }
}
