package decaf.type;

public class PointerType extends Type {

	private Type targetType;

	public Type getTargetType() {
		return targetType;
	}

	public PointerType(Type targetType) {
		this.targetType = targetType;
	}

	@Override
	public boolean compatible(Type type) {
		if (type.equal(BaseType.ERROR())) {
			return true;
		}
		if (!(equal(type) || type.equal(BaseType.NULL())) ) {
			return false;
		}
		return true;
	}

	@Override
	public boolean equal(Type type) {
		if (!type.isPointerType()) {
			return false;
		}
		return targetType.equal(((PointerType) type).targetType);
	}

	@Override
	public String toString() {
		if (isConst()) {
			String str = targetType + "*";
			if (!str.contains("const"))
				return "const "+ str; 
		}
		
		return targetType + "*";
	}

	@Override
	public boolean isPointerType() {
		return true;
	}
}
