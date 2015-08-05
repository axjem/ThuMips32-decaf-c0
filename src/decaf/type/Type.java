package decaf.type;

public abstract class Type {
	
	protected boolean isConst = false;
	
	public boolean isBaseType() {
		return false;
	}

	public boolean isPointerType() {
		return false;
	}

	public boolean isFuncType() {
		return false;
	}
	
	public boolean isConst() {
		return isConst;
	}
	
	public Type setConst(boolean isConst) {
		this.isConst = isConst;
		return this;
	}

	public abstract boolean compatible(Type type);

	public abstract boolean equal(Type type);

	public abstract String toString();
}
