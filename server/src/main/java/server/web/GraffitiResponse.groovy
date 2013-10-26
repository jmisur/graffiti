package server.web

import server.repo.GraffitiData

class GraffitiResponse {

	String user
	String description
	String[] data
	String latitude
	String longitude
	String height
	String width

	GraffitiResponse(GraffitiData data) {
		user = data.user
		description = data.description
		this.data = data.data
		latitude = data.loc[0]
		longitude = data.loc[1]
		height = data.height
		width = data.width
	}
}
