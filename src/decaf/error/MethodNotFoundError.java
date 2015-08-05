package decaf.error;

import decaf.Location;

/**
 * example：method 'getMoney' not found<br>
 * PA2
 */
public class MethodNotFoundError extends DecafError {
	
	private String name;
	
	public MethodNotFoundError(Location location, String name) {
		super(location);
		this.name = name;
	}

	@Override
	protected String getErrMsg() {
		return "method '" + name + "' not found";
	}

}
