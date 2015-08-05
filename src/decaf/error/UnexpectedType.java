package decaf.error;

import decaf.Location;

/**
 * example：type : 'int' give ; type : 'struct' expected<br>
 * PA2
 */
public class UnexpectedType extends DecafError {
	
	private String expected;
	
	private String given;
	
	public UnexpectedType(String expected, String given, Location location) {
		super(location);
		this.expected = expected;
		this.given = given;
	}

	@Override
	protected String getErrMsg() {
		return "type : '" + given + "' given ; type : '" + expected + "' expected"; 
	}

}
