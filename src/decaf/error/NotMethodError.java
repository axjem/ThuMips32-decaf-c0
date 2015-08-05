package decaf.error;

import decaf.Location;

public class NotMethodError extends DecafError {
	
	private String name;
	
	public NotMethodError(Location location, String name) {
		super(location);
		this.name = name;
	}

	@Override
	protected String getErrMsg() {
		return "'" + name + "' is not a method";
	}

}
