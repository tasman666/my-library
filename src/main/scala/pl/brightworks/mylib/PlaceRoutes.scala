package pl.brightworks.mylib

import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

class PlaceRoutes(placeService: PlaceService[IO]) {

  def create(): HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root / "places" =>
      placeService.getPlaces.flatMap(places => Ok(places.asJson))

    case GET -> Root / "places" / UUIDVar(placeId) =>
      placeService.getPlace(placeId).flatMap {
        case Some(place) => Ok(place.asJson)
        case None => NotFound()
      }
  }
}
