package decaf.error;

import decaf.Location;

/**
 * example：* /[] can only be applied to pointer<br>
 * PA2
 */
public class NotPointerError extends DecafError {
	
	public NotPointerError(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "*/[] can only be applied to pointer";
	}

}
