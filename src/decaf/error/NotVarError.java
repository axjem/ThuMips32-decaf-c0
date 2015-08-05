package decaf.error;

import decaf.Location;

/**
 * example：'x' is not a variable<br>
 * PA2
 */
public class NotVarError extends DecafError {

	private String name;
	
	public NotVarError(Location location, String name) {
		super(location);
		this.name = name;
	}
	@Override
	protected String getErrMsg() {
		return "'" + name + "' is not a variable";
	}

}
