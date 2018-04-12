package com.knoldus.product.impl

import akka.{Done, NotUsed}
import com.knoldus.product.impl.command.{AddProduct, DeleteProduct, ProductCommand, UpdateProduct}
import com.knoldus.product.impl.event.{ProductAdded, ProductDeleted, ProductEvent, ProductUpdated}
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}

/**
  * The class ProductEntity
  */
class ProductEntity extends PersistentEntity {

  override type Command = ProductCommand[_]
  override type Event = ProductEvent
  override type State = NotUsed

  override def initialState = NotUsed.getInstance()

  override def behavior =
    Actions()
      .onCommand[AddProduct, Done] {
      case (AddProduct(product), ctx, _) =>
        val event: ProductEvent = ProductAdded(product)
        ctx.thenPersist(event) { _ =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (_, state) =>
        state
    }.onCommand[DeleteProduct, Done] {
      case (DeleteProduct(product), ctx, _) =>
        val event = ProductDeleted(product)
        ctx.thenPersist(event) { _ =>
          ctx.reply(Done)
        }
    }.onEvent {
      case (_, state) =>
        state
    }.onCommand[UpdateProduct, Done] {
      case (UpdateProduct(product), ctx, _) =>
        val event = ProductUpdated(product)
        ctx.thenPersist(event) { _ =>
          ctx.reply(Done.getInstance())
        }
    }.onEvent {
      case (_, state) =>
        state
    }
}

object ProductSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = List(

    //Commands
    JsonSerializer[AddProduct],
    JsonSerializer[UpdateProduct],
    JsonSerializer[DeleteProduct],

    //Events
    JsonSerializer[ProductAdded],
    JsonSerializer[ProductUpdated],
    JsonSerializer[ProductDeleted]
  )
}
