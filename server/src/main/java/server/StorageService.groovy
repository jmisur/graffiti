package server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StorageService {

	@Autowired
	GraffitiRepository repo;

	void save(GraffitiRequest data) {
		def entity =  new GraffitiData(user: data.user, description: data.description, data: data.data, loc: [
			data.latitude,
			data.longitude
		]);
		repo.save(entity);
		println "Saved $entity"
	}
}
