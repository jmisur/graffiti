package server.repo

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.geo.Box
import org.springframework.data.mongodb.repository.MongoRepository

interface GraffitiRepository extends MongoRepository<GraffitiData, String>{

	List<GraffitiData> findByLocWithin(Box b, Sort sort)
}
