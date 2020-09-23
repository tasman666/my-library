package pl.brightworks.mylib

import java.util.UUID

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._

object PlaceService {

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

  case class Place(id: UUID, name: String, placeType: PlaceType)

  implicit def placeEncoder: EntityEncoder[IO, Place] = ???
  implicit def placesEncoder: EntityEncoder[IO, Seq[Place]] = ???

  def getPlace(placeId: UUID): IO[Place] = ???
  def getPlaces(): IO[Seq[Place]] = ???

  val placeServiceRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "places"  =>
      getPlaces().flatMap(Ok(_))
    case GET -> Root / "places" / UUIDVar(placeId) =>
      getPlace(placeId).flatMap(Ok(_))
  }

}
