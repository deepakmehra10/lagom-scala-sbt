package com.knoldus.product.impl.command

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}
import com.knoldus.product.api.Product

sealed trait ProductCommand[R] extends ReplyType[R]

case class AddProduct(product: Product) extends ProductCommand[Done]

object AddProduct {
  implicit val format: Format[AddProduct] = Json.format[AddProduct]
}

case class DeleteProduct(id: String) extends ProductCommand[Done]

object DeleteProduct {
  implicit val format: Format[DeleteProduct] = Json.format[DeleteProduct]
}

case class UpdateProduct(product: Product) extends ProductCommand[Done]

object UpdateProduct {
  implicit val format: Format[UpdateProduct] = Json.format[UpdateProduct]
}
