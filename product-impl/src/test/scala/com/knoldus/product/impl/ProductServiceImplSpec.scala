package com.knoldus.product.impl

import akka.Done
import com.datastax.driver.core.Session
import com.knoldus.product.api.{Product, ProductService}
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraSession
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class ProductServiceImplSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {

  private val server = ServiceTest.startServer(ServiceTest.defaultSetup) { ctx =>
    new ProductApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[ProductService]

  override protected def beforeAll(): Unit = {
    val cassandraSession: CassandraSession = server.application.cassandraSession
    val session: Session = Await.result(cassandraSession.underlying(), 10 seconds)

    //Create the required schema.
    createSchema(session)

    //Add some fake data for testing purpose.
    populateData(session)

  }

  override protected def afterAll() = server.stop()

  private def createSchema(session: Session): Unit = {
    //Create Keyspace
    session.execute("CREATE KEYSPACE IF NOT EXISTS product WITH replication = {'class': 'SimpleStrategy', "
      + "'replication_factor': '1'}")

    //Create table
    session.execute("CREATE TABLE product.product("
      + "id text PRIMARY KEY, "
      + "title text, "
      + "price text, "
      + "description text)")
  }

  private def populateData(session: Session): Unit = {
    val product = Product("1", "Pen", "10", "Use this pen for smooth hand writing.")
    session.execute("INSERT INTO product.product (id, title, price, description) VALUES (?, ?, ?, ?)", product.id,
      product.title, product.price, product.description)
  }

  "Product service" should {
    val product = Product("1", "Pen", "10", "Use this pen for smooth hand writing.")
    "should return product by id" in {
      client.getProduct("1").invoke().map { response =>
        response should ===(product)

      }
    }
  }

  "Product service" should {
    val product = Product("2", "Bat", "1000", "Cricket playing bat.")
    "should add a new Product" in {
      client.addProduct.invoke(product).map { response =>
        response should ===(Done.getInstance())

      }
    }
  }

  "Product service" should {
    val product = Product("1", "Pen updated", "100", "updated description.")
    "should update a Product" in {
      client.updateProduct("1").invoke(product).map { response =>
        response should ===(Done.getInstance())

      }
    }
  }

  "Product service" should {
    "should delete a Product" in {
      client.deleteProduct("1").invoke().map { response =>
        response should ===(Done.getInstance())
      }
    }
  }

  "Product service" should {
    val product = Product("1", "Pen", "10", "Use this pen for smooth hand writing.")
    "should return all the products" in {
      client.getAllProduct().invoke().map { response =>
        response should ===(List(product))
      }
    }
  }
}
