package decaf.backend;

public final class OffsetCounter {
	public enum Kind {
		LOCAL, PARAMETER, VARFIELD, GLOBAL
	}
	
	public static final int BYTE_SIZE = 1;
	
	public static final int WORD_SIZE = 4;

	private static final int[] initValue = new int[] { -3 * WORD_SIZE, WORD_SIZE, WORD_SIZE, 0 };

	private static final int[] direction = new int[] { -1, 1, 1, 1 };

	public static final OffsetCounter LOCAL_OFFSET_COUNTER = new OffsetCounter(Kind.LOCAL);

	public static final OffsetCounter PARAMETER_OFFSET_COUNTER = new OffsetCounter(Kind.PARAMETER);

//	public static final OffsetCounter VARFIELD_OFFSET_COUNTER = new OffsetCounter(Kind.VARFIELD);
	
	public static final OffsetCounter GLOBAL_OFFSET_COUNTER = new OffsetCounter(Kind.GLOBAL);

	private Kind kind;

	private int value;

	private OffsetCounter(Kind kind) {
		this.kind = kind;
		reset();
	}

	public void reset() {
		value = initValue[kind.ordinal()];
	}

	public int next(int value) {
		int ret = this.value;
		this.value += direction[kind.ordinal()] * value;
		return ret;
	}

	public void set(int offset) {
		value = offset;
	}
}
