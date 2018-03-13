package com.knoldus.product.impl.event

import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventShards ,AggregateEventTag, AggregateEventTagger}
import play.api.libs.json.{Format, Json}
import com.knoldus.product.api.Product

sealed trait ProductEvent extends AggregateEvent[ProductEvent] {
  override def aggregateTag: AggregateEventShards[ProductEvent] = ProductEvent.Tag
}

object ProductEvent {
  val NumShards = 3
  val Tag: AggregateEventShards[ProductEvent] = AggregateEventTag.sharded[ProductEvent](NumShards)
}

case class ProductAdded(product: Product) extends ProductEvent

object ProductAdded {
  implicit val format: Format[ProductAdded] = Json.format[ProductAdded]

}

case class ProductDeleted(id: String) extends ProductEvent

object ProductDeleted {
  implicit val format: Format[ProductDeleted] = Json.format[ProductDeleted]

}

case class ProductUpdated(product: Product) extends ProductEvent

object ProductUpdated {
  implicit val format: Format[ProductUpdated] = Json.format[ProductUpdated]

}
