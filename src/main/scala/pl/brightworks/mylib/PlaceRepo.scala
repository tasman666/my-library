package pl.brightworks.mylib

import java.util.UUID

import cats.effect.IO

trait PlaceRepo[F[_]] {

  def find(placeId: UUID): F[Option[Place]]

  def findAll(): F[Seq[Place]]

}

class InMemoryPlaceRepo extends PlaceRepo[IO] {

  val values = Seq(
    Place(UUID.randomUUID(), "my tablet", Tablet),
    Place(UUID.randomUUID(), "work tablet", Tablet),
    Place(UUID.randomUUID(), "my phone", Phone),
    Place(UUID.randomUUID(), "room", Shelf),
  )

  override def find(placeId: UUID): IO[Option[Place]] =
    IO(values.find(_.id == placeId))

  override def findAll(): IO[Seq[Place]] = IO(values)
}