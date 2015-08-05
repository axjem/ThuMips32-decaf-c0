package decaf.error;

import decaf.Location;

/**
 * example：subscript must be an integer<br>
 * PA2
 */
public class NotIntError extends DecafError {

	public NotIntError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "subscript must be an integer";
	}

}
