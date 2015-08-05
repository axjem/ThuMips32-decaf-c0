package decaf.error;

import decaf.Location;

/**
 * example：NewExpr base type must be non-void type<br>
 * PA2
 */
public class BadNewElementError extends DecafError {

	public BadNewElementError(Location location) {
		super(location);
	}
	
	@Override
	protected String getErrMsg() {
		return "new element must be non-void type";
	}

}
