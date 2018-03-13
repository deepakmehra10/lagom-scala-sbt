package com.knoldus.welcome.impl

import com.knoldus.welcome.api.{User, WelcomeService}
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

/**
  * Implementation of the WelcomeService.
  */
class WelcomeServiceImpl extends WelcomeService {

  var user = ListBuffer[User](User(1, "Deepak Mehra"), User(2, "Abhishek Giri"))

  override def welcome(name: String) = ServiceCall { _ =>
    Future.successful(s"Welcome, $name")
  }

  override def addUser() = ServiceCall { request =>
    //request::user
    print(user+"heyyyy")
    //val fresh = request+=user
    user+=request
    print("\n\n\n\n")
    //print(request::user)
    print("\n\n\n\n")
      //print(fresh)
    Future.successful("User created successfully");
  }

  override def getUser(id: Int) = ServiceCall { _ =>
    println(user+"before call")

    print("\n\n\n\n")
    println(user+"List")
    Future.successful(user.filter(a => a.id == id).headOption.fold(throw new RuntimeException)(user => user));
  }
}
