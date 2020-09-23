package pl.brightworks.mylib

import cats.effect._
import cats.implicits._
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.blaze._
import org.http4s.server.Router

import scala.concurrent.ExecutionContext.global

object LibraryApp extends IOApp {

  def run(args: List[String]) = {
    val services = LibraryService.libraryServiceRoutes <+> PlaceService.placeServiceRoutes

    val httpApp = Router("/api" -> services).orNotFound

    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

  }

}
