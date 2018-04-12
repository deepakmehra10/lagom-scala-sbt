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


  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup.withCassandra(true)) { ctx =>
    new ProductApplication(ctx) with LocalServiceLocator
  }

 lazy val client = server.serviceClient.implement[ProductService]

  override protected def beforeAll(): Unit = {
    server
   println("\n\n\n\n\n\n\n\n"+"inside beforeALlllll"+ "\n\n\n\n\n\n\n\n")
    val session: CassandraSession = server.application.cassandraSession
    //val session: Session = Await.result(cassandraSession.underlying(), 30 seconds)

    //println(session.execute("desc keyspaces;").one())
    //Create the required schema.
    createSchema(session)

    //Add some fake data for testing purpose.
    populateData(session)

  }

  override protected def afterAll() = server.stop()

  private def createSchema(session: CassandraSession): Unit = {

    //Create Keyspace
   val createKeyspace = session.executeWrite("CREATE KEYSPACE IF NOT EXISTS product WITH replication = {'class': 'SimpleStrategy','replication_factor': '1'};")
   Await.result(createKeyspace, 20.seconds)
    // println(session.execute("CREATE KEYSPACE IF NOT EXISTS product WITH replication = {'class': 'SimpleStrategy','replication_factor': '1'}").one()+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n")*/
    //println("\n\n\n\n\n\n\n\n hehehehehe"+ session.execute("desc keyspaces;")+"\n\n\n\n\n\n\n\n\n")

    //Create table
    val createTable = session.executeCreateTable("""CREATE TABLE IF NOT EXISTS product (
                      |id text PRIMARY KEY, title text, price text, description text)""".stripMargin)
  Await.result(createTable, 20.seconds)
  }

  private def populateData(session: CassandraSession): Unit = {
    val product = Product("1", "Pen", "10", "Use this pen for smooth hand writing.")
    val insertProduct = session.executeWrite("INSERT INTO product (id, title, price, description) VALUES (?, ?, ?, ?)", product.id,
      product.title, product.price, product.description)
    Await.result(insertProduct, 20.seconds)
  }

  "Product service" should {
    val product = Product("1", "Pen", "10", "Use this pen for smooth hand writing.")
    "should return product by id" in {
      client.getProduct("1").invoke().map { response =>
        println(response + "response")
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
