package com.knoldus.product.impl.event

import com.knoldus.product.api.Product
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards, AggregateEventTag}
import play.api.libs.json.{Format, Json}

/**
  * The ProductEvent trait.
  */
sealed trait ProductEvent extends AggregateEvent[ProductEvent] {
  override def aggregateTag: AggregateEventShards[ProductEvent] = ProductEvent.Tag
}

object ProductEvent {
  val NumShards = 3
  val Tag: AggregateEventShards[ProductEvent] = AggregateEventTag.sharded[ProductEvent](NumShards)
}

/**
  * Product Add Event.
  *
  * @param product - The product object.
  */
case class ProductAdded(product: Product) extends ProductEvent

object ProductAdded {
  implicit val format: Format[ProductAdded] = Json.format[ProductAdded]

}

/**
  * Product Delete Event.
  *
  * @param id - The product id.
  */
case class ProductDeleted(id: String) extends ProductEvent

object ProductDeleted {
  implicit val format: Format[ProductDeleted] = Json.format[ProductDeleted]

}

/**
  * Product Update Event
  *
  * @param product - The product object.
  */
case class ProductUpdated(product: Product) extends ProductEvent

object ProductUpdated {
  implicit val format: Format[ProductUpdated] = Json.format[ProductUpdated]

}
