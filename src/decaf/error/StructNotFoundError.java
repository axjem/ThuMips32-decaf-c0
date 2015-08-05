package decaf.error;

import decaf.Location;

/**
 * example：struct 'zig' not found<br>
 * PA2
 */
public class StructNotFoundError extends DecafError {

	private String name;
	
	public StructNotFoundError(Location location, String name) {
		super(location);
		this.name = name;
	}
	
	@Override
	protected String getErrMsg() {
		return "struct '" + name + "' not found";
	}

}
