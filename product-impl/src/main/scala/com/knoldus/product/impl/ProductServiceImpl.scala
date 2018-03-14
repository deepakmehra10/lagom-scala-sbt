package com.knoldus.product.impl

import akka.{Done, NotUsed}
import com.knoldus.product.api.{Product, ProductService}
import com.knoldus.product.impl.command.{AddProduct, DeleteProduct, UpdateProduct}
import com.knoldus.product.impl.constants.QueryConstants
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{ExceptionMessage, NotFound, TransportErrorCode}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession

import scala.concurrent.ExecutionContext

class ProductServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, session: CassandraSession)
                        (implicit ec: ExecutionContext)
  extends ProductService {

  override def getProduct(id: String): ServiceCall[NotUsed, Product] = ServiceCall { _ =>
    session.selectOne(QueryConstants.GET_PRODUCT, id).map {
      case Some(row) => Product.apply(row.getString("id"), row.getString("description"),
        row.getString("price"), row.getString("title"))
      case None => throw new NotFound(TransportErrorCode.NotFound, new ExceptionMessage("Product Id Not Found",
        "Product with this product id does not exist"))
    }
  }

  override def getAllProduct(): ServiceCall[NotUsed, List[Product]] = ServiceCall { _ =>
    session.selectAll(QueryConstants.GET_ALL_PRODUCTS).map {
      row =>
        row.map(product => Product(product.getString("id"), product.getString("description"),
          product.getString("title"), product.getString("price"))).toList
    }
  }

  override def addProduct: ServiceCall[Product, Done] = ServiceCall { request =>
    persistentEntityRegistry.refFor[ProductEntity](request.id).ask(AddProduct(request))
      .map(_ => Done.getInstance())
  }


  override def deleteProduct(id: String): ServiceCall[NotUsed, Done] = ServiceCall { _ =>
    persistentEntityRegistry.refFor[ProductEntity](id).ask(DeleteProduct(id)).map(_ => Done.getInstance())

  }

  override def updateProduct(id: String): ServiceCall[Product, Done] = ServiceCall { request =>
    persistentEntityRegistry.refFor[ProductEntity](id).ask(UpdateProduct(request)).map(_ => Done.getInstance())

  }

}
