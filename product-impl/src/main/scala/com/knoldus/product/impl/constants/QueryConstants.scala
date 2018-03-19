package com.knoldus.product.impl.constants

/**
  * The object QueryConstants.
  */
object QueryConstants {

  // Get product from database by product id.
  val GET_PRODUCT = "SELECT * FROM PRODUCT WHERE id =?"

  // Get all the products from database.
  val GET_ALL_PRODUCTS = "SELECT * FROM PRODUCT"
}
