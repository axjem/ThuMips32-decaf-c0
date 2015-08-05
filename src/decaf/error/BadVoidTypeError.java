package decaf.error;

import decaf.Location;

/**
 * example：void type is not legal here.<br>
 * PA2
 */
public class BadVoidTypeError extends DecafError {

	public BadVoidTypeError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "void type is not legal here.";
	}

}
