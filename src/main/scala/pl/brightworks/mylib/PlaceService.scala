package pl.brightworks.mylib

import java.util.UUID

class PlaceService[F[_]](placeRepo: PlaceRepo[F]) {

  def getPlace(id: UUID): F[Option[Place]] = placeRepo.find(id)

  def getPlaces: F[Seq[Place]] = placeRepo.findAll()

}
