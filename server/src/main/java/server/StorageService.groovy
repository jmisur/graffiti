package server

import javax.annotation.PostConstruct

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.geo.Box
import org.springframework.data.mongodb.core.geo.Point
import org.springframework.data.mongodb.core.index.GeospatialIndex
import org.springframework.stereotype.Service

import server.app.GraffitiRequest;
import server.repo.GraffitiData;
import server.repo.GraffitiRepository;

@Service
class StorageService {

	@Autowired
	GraffitiRepository repo

	@Autowired
	MongoTemplate template

	@PostConstruct
	void setup() {
		template.indexOps(GraffitiData.class).ensureIndex( new GeospatialIndex("loc") );
	}

	void save(GraffitiRequest data) {
		def entity =  new GraffitiData(user: data.user, description: data.description, data: data.data, loc: [
			data.latitude,
			data.longitude
		]);
		repo.save(entity);
		println "Saved $entity"
	}

	List<GraffitiData> find(String lat0, String long0, String lat1, String long1) {
		repo.findByLocWithin( new Box( new Point(lat0 as double, long0 as double), new Point(lat1 as double,long1 as double)) );
	}
}
