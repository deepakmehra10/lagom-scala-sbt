package com.knoldus.welcome.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json._

/**
  * The welcome service interface.
  * <p>
  * This describes everything that Lagom needs to know about how to serve and
  * consume the WelcomeService.
  */
trait WelcomeService extends Service {

  /**
    * Example: curl http://localhost:9000/api/welcome/Deepak
    */
  def welcome(name: String): ServiceCall[NotUsed, String]

  def addUser: ServiceCall[User, String]

  def getUser(id: Int): ServiceCall[NotUsed, User]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("welcome")
      .withCalls(
        pathCall("/api/welcome/:name", welcome _),
        restCall(Method.POST, "/api/welcome/postUser", addUser _),
        restCall(Method.GET, "/api/welcome/user/:id", getUser _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}

case class User(id: Int, name: String)

object User {
  implicit val format: Format[User] = Json.format[User]
}