package decaf.error;

import decaf.Location;

public class IncompatInitError extends DecafError {

	private String left;

	private String right;

	public IncompatInitError(Location location, String left, String right) {
		super(location);
		this.left = left;
		this.right = right;
	}

	@Override
	protected String getErrMsg() {
		return "incompatible init variable of '" + left + "' + type with constant of '" + right + "' type";
	}

}