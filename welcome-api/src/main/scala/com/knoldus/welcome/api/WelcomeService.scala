package com.knoldus.welcome.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

/**
  * The welcome service trait.
  */
trait WelcomeService extends Service {

  /**
    * Example: curl http://localhost:9000/api/welcome/Deepak
    */
  def welcome(name: String): ServiceCall[NotUsed, String]

  override final def descriptor = {
    import Service._
    // @formatter:off
    named("welcome")
      .withCalls(
        pathCall("/api/welcome/:name", welcome _),
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}