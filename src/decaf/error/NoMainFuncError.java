package decaf.error;

import decaf.Location;

/**
 * example：no legal main func named 'main' was found<br>
 * PA2
 */
public class NoMainFuncError extends DecafError {

	private String name;
	
	public NoMainFuncError(String name) {
		super(Location.NO_LOCATION);
		this.name = name;
	}
	
	@Override
	protected String getErrMsg() {
		return "no legal main function named '" + name + "' was found";
	}

}
