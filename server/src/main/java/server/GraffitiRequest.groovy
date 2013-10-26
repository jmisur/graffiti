package server

import groovy.transform.Canonical


@Canonical
class GraffitiRequest {

	String user
	String description
	String data;
	String latitude;
	String longitude;
}
