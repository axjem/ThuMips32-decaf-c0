package decaf.error;

import decaf.Location;

/**
 * example：TypeCast failed from 'struct X' to 'struct Y'<br>
 * PA2
 */
public class TypeCastError extends DecafError {
	
	private String type;
	
	private String exprType;
	
	public TypeCastError(String type, String exprType, Location location) {
		super(location);
		this.type = type;
		this.exprType = exprType;
	}

	@Override
	protected String getErrMsg() {
		return "TypeCast failed : from '" + type + "' to '" + exprType + "'";
	}

}
