package decaf.tac;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import decaf.backend.OffsetCounter;
import decaf.machdesc.Register;
import decaf.symbol.Variable;
import decaf.type.BaseType;
import decaf.type.Type;

public class Temp {
	public int id;

	public String name;

	public int offset = Integer.MAX_VALUE;

	public int size;

	public Variable sym;

	public boolean isConst;

	public int value;
	
	public float value_f;

	public boolean isParam;

//	public boolean isLoaded;

	public Register reg;

	public int lastVisitedBB = -1;

	private static int tempCount = 0;

	public static final Comparator<Temp> ID_COMPARATOR = new Comparator<Temp>() {

		@Override
		public int compare(Temp o1, Temp o2) {
			return o1.id > o2.id ? 1 : o1.id == o2.id ? 0 : -1;
		}

	};

	public Temp() {
	}
	
	public boolean isFloat() {
		return name.contains("_F");
	}

	public Temp(int id, String name, int size, int offset) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.offset = offset;
	}

	public static Temp createTempI4() {
		int id = tempCount++;
		int size = 4;
		return new Temp(id, "_T" + id, size, Integer.MAX_VALUE);
	}

	public static Temp createTempF4() {
		int id = tempCount++;
		int size = 4;
		return new Temp(id, "_F" + id, size, Integer.MAX_VALUE);
	}
	
	public static Temp createTemp(Type t) {
		if (t.equal(BaseType.FLOAT())) {
			return createTempF4();
		} else {
			return createTempI4();
		}
	}

	private static Map<Integer, Temp> constTempPool = new HashMap<Integer, Temp>();
	private static Map<Float, Temp> constTempPool_f = new HashMap<Float, Temp>();

	public static Temp createConstTemp(int value) {
		Temp temp = constTempPool.get(value);
		if (temp == null) {
			temp = new Temp();
			temp.isConst = true;
			temp.value = value;
			temp.name = Integer.toString(value);
			constTempPool.put(value, temp);
		}
		return temp;
	}
	
	public static Temp createConstTemp_f(float value) {
		Temp temp = constTempPool_f.get(value);
		if (temp == null) {
			temp = new Temp();
			temp.isConst = true;
			temp.value_f = value;
			temp.name = Float.toString(value);
			constTempPool_f.put(value, temp);
		}
		return temp;
	}

	public boolean isOffsetFixed() {
		return offset != Integer.MAX_VALUE;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Temp) {
			return id == ((Temp) obj).id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

}
