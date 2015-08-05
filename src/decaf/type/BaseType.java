package decaf.type;

public class BaseType extends Type {

	public enum BASETYPE {INT, FLOAT, BOOL, CHAR, NULL, ERROR, VOID};
	
	private BASETYPE tag;

	private BaseType(BASETYPE tag) {
		this.tag = tag;
	}
	
	public static BaseType INT() {
		return new BaseType(BASETYPE.INT);
	}
	public static BaseType FLOAT() {
		return new BaseType(BASETYPE.FLOAT);
	}
	public static BaseType BOOL() {
		return new BaseType(BASETYPE.BOOL);
	}
	public static BaseType CHAR() {
		return new BaseType(BASETYPE.CHAR);
	}
	public static BaseType NULL() {
		return new BaseType(BASETYPE.NULL);
	}
	public static BaseType ERROR() {
		return new BaseType(BASETYPE.ERROR);
	}
	public static BaseType VOID() {
		return new BaseType(BASETYPE.VOID);
	}

	@Override
	public boolean isBaseType() {
		return true;
	}

	@Override
	public boolean compatible(Type type) {
		if (!type.isBaseType())
			return false;
		BaseType t = (BaseType) type;
		if (tag == BASETYPE.ERROR || t.tag == BASETYPE.ERROR) {
			return true;
		}
		if (tag == BASETYPE.INT) {
			return t.tag == BASETYPE.INT || t.tag == BASETYPE.CHAR;
		}
		if (tag == BASETYPE.FLOAT) {
			return t.tag == BASETYPE.FLOAT || t.tag == BASETYPE.INT || t.tag == BASETYPE.CHAR;
		}
		if (tag == BASETYPE.BOOL) {
			return t.tag == BASETYPE.BOOL;
		}
		if (tag == BASETYPE.CHAR) {
			return t.tag == BASETYPE.CHAR;
		}
		return false;
	}

	@Override
	public boolean equal(Type type) {
		if (!type.isBaseType())
			return false;
		return this.tag == ((BaseType) type).tag;
	}

	@Override
	public String toString() {
		switch(tag) {
		case INT:
			if (isConst())
				return "const int";
			else
				return "int";
		case BOOL:
			if (isConst())
				return "const bool";
			else
				return "bool";
		case CHAR:
			if (isConst())
				return "const char";
			else
				return "char";
		case FLOAT:
			if (isConst())
				return "const float";
			else
				return "float";
		case ERROR:
			return "error";
		case NULL:
			return "null";
		case VOID:
			return "void";
		}
		return null;
	}

}
