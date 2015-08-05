package decaf.error;

import decaf.Location;

public class MultiCharError extends DecafError {
	private String str;

	public MultiCharError(Location location, String str) {
		super(location);
		this.str = str;
	}

	@Override
	protected String getErrMsg() {
		return "illegal multiple characters in char constant " + str;
	}
}
