package pl.brightworks.mylib

import java.util.UUID

class LibraryService[F[_]](libraryRepo: LibraryRepo[F]) {

  def getLibrary(id: UUID): F[Option[Library]] = libraryRepo.find(id)

  def getLibraries: F[Seq[Library]] = libraryRepo.findAll()

}
