package com.knoldus.product.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}
import play.api.libs.json.{Format, Json}


trait ProductService extends Service {

  def getProduct(id: String): ServiceCall[NotUsed, Product]

  def getAllProduct(): ServiceCall[NotUsed, List[Product]]

  def addProduct: ServiceCall[Product, Done]

  def deleteProduct(id: String): ServiceCall[NotUsed, Done]

  def updateProduct(id: String): ServiceCall[Product, Done]


  override final def descriptor = {
    import Service._
    // @formatter:off
    named("product")
      .withCalls(
        restCall(Method.GET, "/api/product/getProduct/:id", getProduct _),
        restCall(Method.GET, "/api/product/getAllProducts", getAllProduct _),
        restCall(Method.POST, "/api/product/addProduct", addProduct _),
        restCall(Method.DELETE, "/api/product/deleteProduct/:id", deleteProduct _),
        restCall(Method.PUT, "/api/product/updateProduct/:id", updateProduct _)
      )
      .withAutoAcl(true)
    // @formatter:on
  }
}

case class Product(id: String, title: String, price: String, description: String)

object Product {
  implicit val format: Format[Product] = Json.format[Product]
}
