package decaf.error;

import decaf.Location;

public class UntermCharError extends DecafError {
	private String str;

	public UntermCharError(Location location, String str) {
		super(location);
		this.str = str;
	}

	@Override
	protected String getErrMsg() {
		return "unterminated char constant " + str;
	}
}
