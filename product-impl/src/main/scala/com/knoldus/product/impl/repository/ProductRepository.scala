/*
package com.knoldus.product.impl.repository

import com.knoldus.product.api.Product
import com.lightbend.lagom.scaladsl.api.transport.{ExceptionMessage, NotFound, TransportErrorCode}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

trait ProductRepository {

/*
  def getProductById(id: String): Unit = {
    session.selectOne("select * from product where id = ?", id).map {
      case Some(row) => Product.apply(row.getString("id"), row.getString("description"),
        row.getString("price"), row.getString("title"))
      case None => throw new NotFound(TransportErrorCode.NotFound, new ExceptionMessage("Product Id Not Found",
        "Product with this product id does not exist"))
    }
  }*/
}

object ProductRepository extends ProductRepository
*/
