package com.knoldus.product.impl

import akka.Done
import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.knoldus.product.api.Product
import com.knoldus.product.impl.command.{AddProduct, ProductCommand}
import com.knoldus.product.impl.event.{ProductAdded, ProductEvent}
import com.lightbend.lagom.scaladsl.playjson.JsonSerializerRegistry
import com.lightbend.lagom.scaladsl.testkit.PersistentEntityTestDriver
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers, OptionValues}

class ProductEntitySpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll with OptionValues {

  val system = ActorSystem("ProductEntitySpec", JsonSerializerRegistry.actorSystemSetupFor(ProductSerializerRegistry))

  val product = Product("1", "Pen", "10", "Pen for good handwriting.")

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  private def withDriver[T](block: PersistentEntityTestDriver[ProductCommand[_], ProductEvent, Product] => T): T = {
    val driver = new PersistentEntityTestDriver(system, new ProductEntity, "1")
    try {
      block(driver)
    } finally {
      driver.getAllIssues shouldBe empty
    }
  }

  "The product entity" should {

    "allow creating a product" in withDriver { driver =>
      val outcome = driver.run(AddProduct(product))
      outcome.events should contain only ProductAdded(product)
        //outcome.replies should contain only Done
    }

  }
}
