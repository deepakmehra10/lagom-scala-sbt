package com.knoldus.product.impl.event

import akka.Done
import com.datastax.driver.core.{BoundStatement, PreparedStatement}
import com.knoldus.product.api.Product
import com.lightbend.lagom.scaladsl.persistence.cassandra.{CassandraReadSide, CassandraSession}
import com.lightbend.lagom.scaladsl.persistence.{AggregateEventTag, ReadSideProcessor}

import scala.concurrent.{ExecutionContext, Future}

class ProductEventReadSideProcessor(db: CassandraSession, readSide: CassandraReadSide
                                   )(implicit ec: ExecutionContext) extends ReadSideProcessor[ProductEvent] {

  private var insertProduct: PreparedStatement = _

  private var deleteProduct: PreparedStatement = _

  private var updateProduct: PreparedStatement = _

  override def buildHandler() = readSide.builder[ProductEvent]("ProductEventReadSideProcessor")
    .setGlobalPrepare(createTable)
    .setPrepare(_ => prepareInsertProduct())
    .setEventHandler[ProductAdded](ese => insertProduct(ese.event.product))
    //.setPrepare(_ => prepareDeleteProduct())
    .setEventHandler[ProductDeleted](ese => processProductDeleted(ese.event.id))
    .setEventHandler[ProductUpdated](ese => updateProduct(ese.event.product))
    .build()

  override def aggregateTags: Set[AggregateEventTag[ProductEvent]] = ProductEvent.Tag.allTags

  // Helpers -----------------------------------------------------------------------------------------------------------

  private def createTable() = {
    db.executeCreateTable(
      """CREATE TABLE IF NOT EXISTS product.product (
        |id text PRIMARY KEY, title text, price text, description text)""".stripMargin)
  }

  private def prepareInsertProduct(): Future[Done] =
    db.prepare("INSERT INTO product.product (id, title, price, description) VALUES (?, ?, ?, ?)")
      .map { ps =>
        insertProduct = ps
        Done
      }.map(_ => db.prepare("DELETE FROM product where id = ?").map(ps => {
        deleteProduct = ps
        Done
      })).map(_ => db.prepare("update product set price=?,title=?,description=? where id =?").map(ps => {
      updateProduct = ps
      println("Successfully updated")
      Done.getInstance()
    })).flatten

  private def insertProduct(product: Product): Future[List[BoundStatement]] = {
    val bindInsertProduct = insertProduct.bind()
    bindInsertProduct.setString("id", product.id)
    bindInsertProduct.setString("title", product.title)
    bindInsertProduct.setString("price", product.price)
    bindInsertProduct.setString("description", product.description)
    Future.successful(List(bindInsertProduct))
  }

  private def prepareDeleteProduct() = {
    db.prepare("DELETE FROM product where id = ?")
      .map(ps => {
        deleteProduct = ps
        Done
      })

  }

  /*
    private def setDeleteProduct(deleteProduct: PreparedStatement): Unit =
      this.deleteProduct = deleteProduct
  */

  private def processProductDeleted(id: String) = {
    val bindDeleteProduct = deleteProduct.bind()
    //println(s"DEBUG Within processFriendRemoved userId: ${event.friendId} followedBy: ${event.userId}")
    bindDeleteProduct.setString("id", id)
    Future.successful(List(bindDeleteProduct))
  }

  private def updateProduct(product: Product): Future[List[BoundStatement]] = {
    val bindUpdateProduct = updateProduct.bind()
    bindUpdateProduct.setString("id", product.id)
    bindUpdateProduct.setString("title", product.title)
    bindUpdateProduct.setString("price", product.price)
    bindUpdateProduct.setString("description", product.description)
    Future.successful(List(bindUpdateProduct))
  }

}

