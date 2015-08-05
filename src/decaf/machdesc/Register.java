package decaf.machdesc;

import decaf.tac.Temp;

public abstract class Register {

	public Temp var;

	public abstract boolean isfloat();
	
	public abstract String toString();
}
