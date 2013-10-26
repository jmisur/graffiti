package server

import org.springframework.data.mongodb.repository.MongoRepository

interface GraffitiRepository extends MongoRepository<GraffitiData, String>{
}
