package pl.brightworks.mylib

import java.util.UUID

import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.{HttpRoutes, _}
import org.http4s.circe._
import org.http4s.dsl.io._

object LibraryService {

  case class Library(id: UUID, name: String)

  val values = Seq(
    Library(UUID.randomUUID(), "book library"),
    Library(UUID.randomUUID(), "cd library"),
  )

  def getLibrary(id: UUID): IO[Option[Library]] = IO(values.find(_.id == id))
  def getLibraries: IO[Seq[Library]] = IO(values)

  val libraryServiceRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "libraries"  =>
      getLibraries.flatMap(libraries => Ok(libraries.asJson))
    case GET -> Root / "libraries" / UUIDVar(libraryId) =>
      getLibrary(libraryId).flatMap {
      case Some(library) => Ok(library.asJson)
      case None => NotFound()
    }
  }

}
