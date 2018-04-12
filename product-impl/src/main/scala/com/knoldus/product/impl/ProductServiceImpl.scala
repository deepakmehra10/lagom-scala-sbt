package com.knoldus.product.impl

import akka.{Done, NotUsed}
import com.knoldus.product.api.{Product, ProductService}
import com.knoldus.product.impl.command.{AddProduct, DeleteProduct, UpdateProduct}
import com.knoldus.product.impl.constants.QueryConstants
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.{ExceptionMessage, NotFound, TransportErrorCode}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

/**
  * Implementation of the ProductService
  *
  * @param persistentEntityRegistry - Persistent Entity
  * @param session                  - Cassandra Session
  * @param ec                       - Execution context
  */
class ProductServiceImpl(persistentEntityRegistry: PersistentEntityRegistry, session: CassandraSession)
                        (implicit ec: ExecutionContext)
  extends ProductService {

  private final val log: Logger = LoggerFactory.getLogger(classOf[ProductServiceImpl])

  /**
    *
    * @param id - The Product's id
    * @return
    */
  override def getProduct(id: String): ServiceCall[NotUsed, Product] = ServiceCall { _ =>
    session.selectOne(QueryConstants.GET_PRODUCT, id).map {
      case Some(row) => Product.apply(row.getString("id"), row.getString("title"), row.getString("price"),
        row.getString("description"))
      case None => throw new NotFound(TransportErrorCode.NotFound, new ExceptionMessage("Product Id Not Found",
        "Product with this product id does not exist"))
    }
  }

  /**
    *
    * @return
    */
  override def getAllProduct(): ServiceCall[NotUsed, List[Product]] = ServiceCall { _ =>
    session.selectAll(QueryConstants.GET_ALL_PRODUCTS).map {
      row =>
        log.info("Getting list of all the products.")
        row.map(product => Product(product.getString("id"), product.getString("title"), product.getString("price"),
          product.getString("description"))).toList
    }
  }

  /**
    *
    * @return
    */
  override def addProduct: ServiceCall[Product, Done] =
    ServiceCall { request =>
    persistentEntityRegistry
      .refFor[ProductEntity](request.id).ask(AddProduct(request))
      .map(_ => {
        log.info(s"Product with product id ${request.id}, " +
          s"successfully added. ")
        Done
      })
  }

  /**
    *
    * @param id - The Product's id
    * @return
    */
  override def deleteProduct(id: String): ServiceCall[NotUsed, Done] = ServiceCall { _ =>
    persistentEntityRegistry.refFor[ProductEntity](id)
      .ask(DeleteProduct(id)).map(_ => {
      log.info(s"Product with product id ${id}, successfully deleted.")
      Done.getInstance()
    })

  }

  /**
    *
    * @param id - The Product's id
    * @return
    */
  override def updateProduct(id: String): ServiceCall[Product, Done] =
    ServiceCall { request =>
    persistentEntityRegistry
      .refFor[ProductEntity](id).ask(UpdateProduct(request))
      .map(_ => {
      log.info(s"Product with product id ${id}, successfully " +
        s"updated.")
      Done.getInstance()
    })

  }
}
