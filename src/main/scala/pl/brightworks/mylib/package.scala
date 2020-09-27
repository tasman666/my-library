package pl.brightworks

import java.util.UUID

package object mylib {

  case class Library(id: UUID, name: String)

  case class Place(id: UUID, name: String, placeType: PlaceType)

  sealed trait PlaceType {
    val name: String
  }

  case object Phone extends PlaceType {
    override val name: String = "phone"
  }
  case object Tablet extends PlaceType {
    override val name: String = "tablet"
  }
  case object Shelf extends PlaceType {
    override val name: String = "shelf"
  }

}
