package com.knoldus.product.api

import play.api.libs.json.{Format, Json}

/**
  *
  * @param id - The Product's id.
  * @param title - The Product's title.
  * @param price - The Product's price.
  * @param description - The Product's description.
  */
case class Product(id: String, title: String, price: String, description: String)

object Product {
  implicit val format: Format[Product] = Json.format[Product]
}
