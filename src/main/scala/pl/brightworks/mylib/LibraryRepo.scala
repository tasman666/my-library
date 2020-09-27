package pl.brightworks.mylib

import java.util.UUID

import cats.effect.IO

trait LibraryRepo[F[_]] {

  def find(libraryId: UUID): F[Option[Library]]

  def findAll(): F[Seq[Library]]

}

class InMemoryLibraryRepo extends LibraryRepo[IO] {

  val values = Seq(
    Library(UUID.randomUUID(), "book library"),
    Library(UUID.randomUUID(), "cd library"),
  )

  override def find(libraryId: UUID): IO[Option[Library]] =
    IO(values.find(_.id == libraryId))

  override def findAll(): IO[Seq[Library]] = IO(values)
}
