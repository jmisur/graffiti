package server


class DataUtils {

	static convertToStreet(List<String> polylines) {
		StringBuilder b = new StringBuilder()

		polylines.each {
			String[] points = it.split(" ")
			b.append('M ')
			def	start = true
			points.each {
				String[] xy = it.split(",")
				if (start) {
					start = false
				} else {
					b.append('L ')
				}

				b.append("${xy[0]} ${xy[1]} ")
			}
		}

		b.toString()
	}
}
