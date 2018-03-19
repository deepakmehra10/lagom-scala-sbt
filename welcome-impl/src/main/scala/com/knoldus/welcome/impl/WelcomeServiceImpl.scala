package com.knoldus.welcome.impl

import com.knoldus.welcome.api.WelcomeService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

/**
  * Implementation of the WelcomeService.
  */
class WelcomeServiceImpl extends WelcomeService {

  private final val log: Logger = LoggerFactory.getLogger(classOf[WelcomeServiceImpl])

  /**
    *
    * @param name - Name of the user to be greeted.
    * @return
    */
  override def welcome(name: String) = ServiceCall { _ =>
    log.info(s"Person with name $name, greeted.")
    Future.successful(s"Welcome, $name")
  }
}
