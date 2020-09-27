package pl.brightworks.mylib

import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._

class LibraryRoutes(libraryService: LibraryService[IO]) {

  def create(): HttpRoutes[IO] = HttpRoutes.of[IO] {

    case GET -> Root / "libraries"  =>
      libraryService.getLibraries.flatMap(libraries => Ok(libraries.asJson))

    case GET -> Root / "libraries" / UUIDVar(libraryId) =>

      libraryService.getLibrary(libraryId).flatMap {
        case Some(library) => Ok(library.asJson)
        case None => NotFound()
      }
  }
}
