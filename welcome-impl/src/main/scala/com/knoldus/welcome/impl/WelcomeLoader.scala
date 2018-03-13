package com.knoldus.welcome.impl

import com.knoldus.welcome.api.WelcomeService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class WelcomeLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new WelcomeApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new WelcomeApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[WelcomeService])
}

abstract class WelcomeApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    // with CassandraPersistenceComponents
    // with LagomKafkaComponents
    with AhcWSComponents {

  // Bind the service that this server provides
  override lazy val lagomServer = serverFor[WelcomeService](wire[WelcomeServiceImpl])

  // Register the JSON serializer registry
  //override lazy val jsonSerializerRegistry = WelcomeSerializerRegistry
}

/*
object WelcomeSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[User],
  )
}*/
