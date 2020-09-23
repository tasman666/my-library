package pl.brightworks.mylib

import java.util.UUID

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._

import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._

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

  val values = Seq(
    Place(UUID.randomUUID(), "my tablet", Tablet),
    Place(UUID.randomUUID(), "work tablet", Tablet),
    Place(UUID.randomUUID(), "my phone", Phone),
    Place(UUID.randomUUID(), "room", Shelf),
  )

  case class Place(id: UUID, name: String, placeType: PlaceType)

  def getPlace(placeId: UUID): IO[Option[Place]] = IO(values.find(_.id == placeId))
  def getPlaces: IO[Seq[Place]] = IO(values)

  val placeServiceRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "places" =>
      getPlaces.flatMap(places => Ok(places.asJson))
    case GET -> Root / "places" / UUIDVar(placeId) =>
      getPlace(placeId).flatMap {
        case Some(place) => Ok(place.asJson)
        case None => NotFound()
      }
  }

}
