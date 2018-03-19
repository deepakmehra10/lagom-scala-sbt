package com.knoldus.product.impl.command

import akka.Done
import com.knoldus.product.api.Product
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import play.api.libs.json.{Format, Json}

/**
  * The ProductCommand trait.
  *
  * @tparam R
  */
sealed trait ProductCommand[R] extends ReplyType[R]

/**
  * Add Product Command.
  *
  * @param product - The product object.
  */
case class AddProduct(product: Product) extends ProductCommand[Done]

object AddProduct {
  implicit val format: Format[AddProduct] = Json.format[AddProduct]
}

/**
  * Delete Product Command.
  *
  * @param id - The product's id.
  */
case class DeleteProduct(id: String) extends ProductCommand[Done]

object DeleteProduct {
  implicit val format: Format[DeleteProduct] = Json.format[DeleteProduct]
}

/**
  * Update product command.
  *
  * @param product - The product object
  */
case class UpdateProduct(product: Product) extends ProductCommand[Done]

object UpdateProduct {
  implicit val format: Format[UpdateProduct] = Json.format[UpdateProduct]
}
