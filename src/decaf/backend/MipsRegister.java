package decaf.backend;

import decaf.machdesc.Register;

public class MipsRegister extends Register {

	public enum RegId {
		ZERO, AT, V0, V1, A0, A1, A2, A3, K0, K1, GP, SP, FP, RA, T0, T1, T2,
		T3, T4, T5, T6, T7, T8, T9, S0, S1, S2, S3, S4, S5, S6, S7, F0, F2, F4,
		F6, F8, F10, F12, F14, F16, F18, F20, F22, F24, F26, F28, F30
	}

	public final RegId id;

	public final String name;

	public MipsRegister(RegId id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@Override
	public boolean isfloat() {
		return id.ordinal() >= RegId.F0.ordinal();
	}

	@Override
	public String toString() {
		return name;
	}

}
