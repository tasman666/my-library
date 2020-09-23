package pl.brightworks.mylib

import java.util.UUID

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._

object LibraryService {

  case class Library(id: UUID, name: String)

  implicit def libraryEncoder: EntityEncoder[IO, Library] = ???
  implicit def librariesEncoder: EntityEncoder[IO, Seq[Library]] = ???

  def getLibrary(id: UUID): IO[Library] = ???
  def getLibraries(): IO[Seq[Library]] = ???

  val libraryServiceRoutes = HttpRoutes.of[IO] {
    case GET -> Root / "libraries"  =>
      getLibraries().flatMap(Ok(_))
    case GET -> Root / "libraries" / UUIDVar(libraryId) =>
      getLibrary(libraryId).flatMap(Ok(_))
  }

}
