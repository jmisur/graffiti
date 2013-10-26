package server

import groovy.transform.Canonical

import org.springframework.data.mongodb.core.mapping.Document

@Canonical
@Document(collection="graffiti")
class GraffitiData {

	String user
	String description
	String data
	List<String> loc
}
