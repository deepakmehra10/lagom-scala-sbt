/*
package com.knoldus.product.impl

import play.api.libs.json.{Format, Json}
import com.knoldus.product.api.Product

case class ProductState(product: Option[Product]) {
  def addProduct(id: String): ProductState = product match {
    case None => throw new IllegalStateException("Product can't be added before product is created")
    case Some(product) =>
      val newProduct = product.title :+ id
      ProductState(product.copy(title = newProduct))
  }
}


object ProductState {

  implicit val format: Format[ProductState] = Json.format[ProductState]

  def apply(product: Product): ProductState = new ProductState(Some(product))

}*/
