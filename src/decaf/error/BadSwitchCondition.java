package decaf.error;

import decaf.Location;

public class BadSwitchCondition extends DecafError {

	public BadSwitchCondition(Location location) {
		super(location);
	}

	@Override
	protected String getErrMsg() {
		return "switch condition must be of int type";
	}

}
