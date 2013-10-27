package server.repo

import groovy.transform.Canonical

import org.springframework.data.mongodb.core.mapping.Document

@Canonical
@Document(collection="graffiti")
class GraffitiData {

	String user
	String description
	Integer height
	Integer width
	double[] loc
	List<String> data
	Date timestamp
	Double popularity
}
