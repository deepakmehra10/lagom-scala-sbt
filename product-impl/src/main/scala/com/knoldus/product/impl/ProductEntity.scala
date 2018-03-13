/*
package com.knoldus.product.impl

import java.time.LocalDateTime

import akka.Done
import com.fasterxml.jackson.databind.JsonSerializer
import com.knoldus.product.api.Product
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson._
import play.api.libs.json._

class ProductEntity extends PersistentEntity {

  override type Command = ProductCommand[_]
  override type Event = ProductEvent
  override type State = ProductState

  /**
    * The initial state. This is used if there is no snapshotted state to be found.
    */
  override def initialState: ProductState = ProductState(None)

  override def behavior: Behavior = {
    case ProductState(None) => notInitialized
    case ProductState(Some(product)) => initialized
  }

  val onGetProduct = Actions().onReadOnlyCommand[Product, GetProductReply] {
    case (Product(), ctx, state) => ctx.reply(GetProductReply(state.product))
  }

  val onProductAdded = Actions().onEvent {
    case (ProductAdded(Product) => state.addFriend(friendId)
  }


  val initialized = {
    Actions().
      onCommand[AddProduct, Done] {
      case (AddProduct(product), ctx, state) =>
        ctx.invalidCommand(s"Product ${product.title} is already created")
        ctx.done
    }
  }.orElse(onGetProduct).orElse(onProductAdded)




}
  /**
    * An entity can define different behaviours for different states, so the behaviour
    * is a function of the current state to a set of actions.
    */

  /*override def behavior: Behavior = {
    case ProductState(Some(newProduct), _) => Actions().onCommand[AddProduct, Done] {

      // Command handler for the UseGreetingMessage command
      case (AddProduct(newProduct), ctx, state) =>
        // In response to this command, we want to first persist it as a
        // GreetingMessageChanged event
        ctx.thenPersist(
          ProductAdded(newProduct)
        ) { _ =>
          // Then once the event is successfully persisted, we respond with done.
          ctx.reply(Done)
        }

/*
      case (DeleteProduct(id), ctx, state) =>
        // In response to this command, we want to first persist it as a
        // GreetingMessageChanged event
        ctx.thenPersist(
          DeleteProduct(id)
        ) { _ =>
          // Then once the event is successfully persisted, we respond with done.
          ctx.reply(Done)
        }

      case (UpdateProduct(product), ctx, state) =>
        // In response to this command, we want to first persist it as a
        // GreetingMessageChanged event
        ctx.thenPersist(
          UpdateProduct(product)
        ) { _ =>
          // Then once the event is successfully persisted, we respond with done.
          ctx.reply(Done)
        }
*/

    }.onReadOnlyCommand[Product.type, Option[Product]] {

      case (product, ctx, state) =>
        ctx.reply(state.product)

    }.onEvent {

      case (ProductAdded(newProduct), state) => ProductState(Some(newProduct), LocalDateTime.now().toString)

      case (ProductDeleted(newProduct), state) => ProductState(Some(newProduct), LocalDateTime.now().toString)

      case (ProductUpdated(newProduct), state) => ProductState(Some(newProduct), LocalDateTime.now().toString)
    }
  }
}*/

/**
  * The current state held by the persistent entity.
  */
case class ProductState(product: Option[Product], timestamp: String)

object ProductState {
  /**
    * Format for the hello state.
    *
    * Persisted entities get snapshotted every configured number of events. This
    * means the state gets stored to the database, so that when the entity gets
    * loaded, you don't need to replay all the events, just the ones since the
    * snapshot. Hence, a JSON format needs to be declared so that it can be
    * serialized and deserialized when storing to and from the database.
    */
  implicit val format: Format[ProductState] = Json.format
  //:TODO
  //val newEntity = ProductState(None, "")
}

/**
  * This interface defines all the events that the ProductEntity supports.
  */
sealed trait ProductEvent extends AggregateEvent[ProductEvent] {
  def aggregateTag = ProductEvent.Tag
}

object ProductEvent {
  val Tag = AggregateEventTag[ProductEvent]
}


/**
  * Commands
  */

sealed trait ProductCommand[R] extends ReplyType[R]

final case class AddProduct(product: Product) extends ProductCommand[Done]

object AddProduct {
  implicit val format: Format[Product] = Json.format
}

/*final case class UpdateProduct(product: Product) extends ProductCommand[Done]

object UpdateProduct extends ProductCommand[Done] {
  implicit val format: Format[Product] = Json.format
}

final case class DeleteProduct(id: Long) extends ProductCommand[Done]

object DeleteProduct {
  implicit val format: Format[Product] = Json.format
}*/

case class GetProductReply(product: Option[Product])

object GetProductReply {
  implicit val format: Format[GetProductReply] = Json.format[GetProductReply]
}

/**
  * Events
  */

//sealed trait ProductEvent

final case class ProductAdded(product: Product) extends ProductEvent

object ProductAdded {
  implicit val format: Format[ProductAdded] = Json.format
}

/*final case class ProductUpdated(product: Product) extends ProductEvent

object ProductUpdated {
  implicit val format: Format[ProductUpdated] = Json.format
}

final case class ProductDeleted(product: Product) extends ProductEvent

object ProductDeleted {
  implicit val format: Format[ProductDeleted] = Json.format
}*/

object ProductSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(

    //Commands
    JsonSerializer[Product],
    JsonSerializer[GetProductReply],
    JsonSerializer[AddProduct],
    //JsonSerializer[UpdateProduct],
    //JsonSerializer[DeleteProduct],

    //Events
    JsonSerializer[ProductAdded]
    //JsonSerializer[ProductUpdated],
    //JsonSerializer[ProductDeleted]
  )
}*/
