package org.graffiti.grafroid.sensor;

import java.util.ArrayList;
import java.util.List;

class ExtremaFinder {
	private List<SensorPoint> data;

	ExtremaFinder(List<SensorPoint> data) {
		this.data = data;
	}

	public List<SensorPoint> getExtrema() {
		float sum = 0;
		float moment = 0;
		int i = 0;
		float val = 0;

		for (i = 0; i < data.size(); i++) {
			SensorPoint g = data.get(i);

			val = (float) Math.abs(g.mValue);
			sum += val;
			moment += i * val;

		}
		float mean = sum / data.size();
		return numaFindExtrema(data, mean / 2);
	}

	/**
	 * converted from http://tpgit.github.io/Leptonica/numafunc2_8c_source.html
	 * 
	 * @param points
	 * @param delta
	 * @return
	 */
	private List<SensorPoint> numaFindExtrema(List<SensorPoint> points, float delta) {
		int i, n, loc, direction;
		boolean found = false;
		double startval = 0;
		double val = 0;
		double maxval = 0;
		double minval = 0;
		List<SensorPoint> nad = new ArrayList<SensorPoint>();

		n = points.size();

		/*
		 * We don't know if we'll find a peak or valley first, but use the first
		 * element of nas as the reference point. Break when we deviate by
		 * 'delta' from the first point.
		 */
		startval = points.get(0).mValue;
		for (i = 1; i < n; i++) {
			val = points.get(i).mValue;
			if (Math.abs(val - startval) >= delta) {
				found = true;
				break;
			}
		}

		if (!found)
			return nad; /* it's empty */

		/* Are we looking for a peak or a valley? */
		if (val > startval) { /* peak */
			direction = 1;
			maxval = val;
		} else {
			direction = -1;
			minval = val;
		}
		loc = i;

		/*
		 * Sweep through the rest of the array, recording alternating
		 * peak/valley extrema.
		 */
		for (i = i + 1; i < n; i++) {
			val = points.get(i).mValue;
			if (direction == 1 && val > maxval) { /* new local max */
				maxval = val;
				loc = i;
			} else if (direction == -1 && val < minval) { /* new local min */
				minval = val;
				loc = i;
			} else if (direction == 1 && (maxval - val >= delta)) {
				nad.add(points.get(loc)); /* save the current max location */
				direction = -1; /* reverse: start looking for a min */
				minval = val;
				loc = i; /* current min location */
			} else if (direction == -1 && (val - minval >= delta)) {
				nad.add(points.get(loc));/* save the current min location */
				direction = 1; /* reverse: start looking for a max */
				maxval = val;
				loc = i; /* current max location */
			}
		}

		/* Save the final extremum */
		/* numaAddNumber(nad, loc); */
		return nad;
	}
}
