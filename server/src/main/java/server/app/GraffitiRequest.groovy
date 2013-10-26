package server.app

import groovy.transform.Canonical


@Canonical
class GraffitiRequest {

	String user
	String description
	String[] data;
	double latitude;
	double longitude;
}
