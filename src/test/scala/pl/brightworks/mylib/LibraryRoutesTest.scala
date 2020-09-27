package pl.brightworks.mylib

import java.util.UUID

import cats.implicits._
import io.circe._
import io.circe.syntax._
import io.circe.generic.semiauto._
import cats.effect._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class LibraryRoutesTest extends AnyFlatSpec with Matchers {

  "Library API" should "find all libraries" in {
    val lib1 = Library(UUID.randomUUID(), "lib1")
    val lib2 = Library(UUID.randomUUID(), "lib2")
    val repo = createRepo(Seq(lib1, lib2))
    val routes = new LibraryRoutes(new LibraryService[IO](repo)).create()

    val response = routes.orNotFound.run(Request(method = Method.GET, uri = uri"/libraries" ))

    val expectedJson = Json.arr(
        jsonFrom(lib1),
        jsonFrom(lib2),
      )
    check(response, Status.Ok, Some(expectedJson))
  }

  it should "find all libraries when no libraries" in {
    val repo = createRepo(Seq())
    val routes = new LibraryRoutes(new LibraryService[IO](repo)).create()

    val response = routes.orNotFound.run(Request(method = Method.GET, uri = uri"/libraries"))

    val expectedJson = Json.arr()
    check(response, Status.Ok, Some(expectedJson))
  }

  it should "find library by id" in {
    val lib1 = Library(UUID.randomUUID(), "lib1")
    val lib2 = Library(UUID.randomUUID(), "lib2")
    val repo = createRepo(Seq(lib1, lib2))
    val routes = new LibraryRoutes(new LibraryService[IO](repo)).create()

    val response = routes.orNotFound.run(Request(method = Method.GET, uri = new Uri().withPath(s"/libraries/${lib2.id.toString}")))

    val expectedJson = jsonFrom(lib2)
    check(response, Status.Ok, Some(expectedJson))
  }

  it should "not find library by id when id does not exist" in {
    val lib1 = Library(UUID.randomUUID(), "lib1")
    val lib2 = Library(UUID.randomUUID(), "lib2")
    val repo = createRepo(Seq(lib1, lib2))
    val routes = new LibraryRoutes(new LibraryService[IO](repo)).create()

    val response = routes.orNotFound.run(Request(method = Method.GET, uri = new Uri().withPath(s"/libraries/111")))

    check(response, Status.NotFound, None)
  }

  it should "return not found code if no recognized uri" in {
    val lib1 = Library(UUID.randomUUID(), "lib1")
    val lib2 = Library(UUID.randomUUID(), "lib2")
    val repo = createRepo(Seq(lib1, lib2))
    val routes = new LibraryRoutes(new LibraryService[IO](repo)).create()

    val response = routes.orNotFound.run(Request(method = Method.GET, uri = new Uri().withPath(s"/libraries/someting/")))

    check(response, Status.NotFound, None)
  }

  private def jsonFrom(lib: Library) = {
    Json.obj(
      ("id", Json.fromString(lib.id.toString)),
      ("name", Json.fromString(lib.name))
    )
  }


  private def check[A](actual:        IO[Response[IO]],
               expectedStatus: Status,
               expectedBody:   Option[A])(
                implicit ev: EntityDecoder[IO, A]
              ): Boolean =  {
    val actualResp         = actual.unsafeRunSync()
    val statusCheck        = actualResp.status == expectedStatus
    val bodyCheck          = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync().isEmpty)( // Verify Response's body is empty.
      expected => {
        actualResp.as[A].unsafeRunSync() == expected
      }
    )
    statusCheck && bodyCheck
  }

  private def createRepo(libraries: Seq[Library]): LibraryRepo[IO] = {
    new LibraryRepo[IO] {
      override def findAll(): IO[Seq[Library]] = IO.pure(libraries)
      override def find(libraryId: UUID): IO[Option[Library]] = IO.pure(libraries.find(_.id == libraryId) )
    }
  }

}
