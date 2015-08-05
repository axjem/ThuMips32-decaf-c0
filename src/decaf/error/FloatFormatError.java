package decaf.error;

import decaf.Location;

/**
 * 
 */

public class FloatFormatError extends DecafError {
	private String val;

	public FloatFormatError(Location location, String val) {
		super(location);
		this.val = val;
	}

	@Override
	protected String getErrMsg() {
		return "float literal " + val + "'s format is wrong.";
	}
}
