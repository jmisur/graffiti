package server.web

import server.DataUtils
import server.repo.GraffitiData

class GraffitiResponse {

	String id
	String user
	String description
	String[] data
	String streetdata
	String latitude
	String longitude
	String height
	String width
	String popularity

	GraffitiResponse(GraffitiData data) {
		id = data.id
		user = data.user
		description = data.description
		this.data = data.data
		this.streetdata = DataUtils.convertToStreet(data.data)
		latitude = data.loc[0]
		longitude = data.loc[1]
		height = data.height
		width = data.width
		popularity = data.popularity
	}
}
