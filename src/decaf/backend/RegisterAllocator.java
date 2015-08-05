package decaf.backend;

import java.util.HashSet;
import java.util.Random;

import decaf.Driver;
import decaf.dataflow.BasicBlock;
import decaf.machdesc.Register;
import decaf.tac.Tac;
import decaf.tac.Temp;

public class RegisterAllocator {
	private BasicBlock bb;

	private MipsFrameManager frameManager;

	private Register[] general_regs;
	
	private Register[] float_regs;

	private Temp fp;

	public RegisterAllocator(Temp fp, MipsFrameManager frameManager,
			Register[] general_regs, Register[] float_regs) {
		this.fp = fp;
		this.frameManager = frameManager;
		this.general_regs = general_regs;
		this.float_regs = float_regs;
	}

	public void reset() {
		frameManager.reset();
	}

	public void alloc(BasicBlock bb) {
		this.bb = bb;
		clear();

		Tac tail = null;
		for (Tac tac = bb.tacList; tac != null; tail = tac, tac = tac.next) {
			switch (tac.opc) {
			case ADD:
			case SUB:
			case MUL:
			case DIV:
			case MOD:
			case LAND:
			case LOR:
			case GTR:
			case GEQ:
			case EQU:
			case NEQ:
			case LEQ:
			case LES:
				findRegForRead(tac, tac.op1);
				findRegForRead(tac, tac.op2);
				findRegForWrite(tac, tac.op0);
				break;
			case NEG:
			case LNOT:
			case CVT_F:
			case CVT_I:
			case MOVEFI:
			case MOVEIF:
			case ASSIGN:
				findRegForRead(tac, tac.op1);
				findRegForWrite(tac, tac.op0);
				break;
			case LOAD_GLOBAL:
			case LOAD_FMM4:
			case LOAD_IMM4:
			case LOAD_STR_CONST:
				findRegForWrite(tac, tac.op0);
				break;
			case INDIRECT_CALL:
				findRegForRead(tac, tac.op1);
			case DIRECT_CALL:
				if (tac.op0 != null) {
					findRegForWrite(tac, tac.op0);
				}
				frameManager.finishActual();
				tac.saves = new HashSet<Temp>();
				for (Temp t : tac.liveOut) {
					if (t.reg != null && t.equals(t.reg.var)
							&& !t.equals(tac.op0)) {
						frameManager.findSlot(t);
						tac.saves.add(t);
					}
				}
				break;
			case PARM:
				findRegForRead(tac, tac.op0);
				int offset = frameManager.addActual(tac.op0);
				tac.op1 = Temp.createConstTemp(offset);
				break;
			case LOAD_B:
			case LOAD:
				findRegForRead(tac, tac.op1);
				findRegForWrite(tac, tac.op0);
				break;
			case STORE_B:
			case STORE:
				findRegForRead(tac, tac.op1);
				findRegForRead(tac, tac.op0);
				break;
			case BRANCH:
			case BEQZ:
			case BNEZ:
			case RETURN:
				throw new IllegalArgumentException();
			}
		}

		bb.saves = new HashSet<Temp>();
		for (Temp t : bb.liveOut) {
			if (t.reg != null && t.equals(t.reg.var)) {
				frameManager.findSlot(t);
				bb.saves.add(t);
			}
		}
		
		switch (bb.endKind) {
		case BY_RETURN:
		case BY_BEQZ:
		case BY_BNEZ:
			if (bb.var != null) {
				if (bb.var.reg != null && bb.var.equals(bb.var.reg.var)) {
					bb.varReg = bb.var.reg;
					return;
				} else {
					// all live temps have been spilled out, so use any reg is valid
					if (bb.var.isFloat()) {
						bb.var.reg = float_regs[0];
					} else {
						bb.var.reg = general_regs[0];
					}
					if (!bb.var.isOffsetFixed()) {
						Driver.getDriver()
								.getOption()
								.getErr()
								.println(bb.var + " may used before define during register allocation");
						frameManager.findSlot(bb.var);
					}
					Tac load = Tac.genLoad(bb.var, fp, Temp.createConstTemp(bb.var.offset));
					bb.insertAfter(load, tail);
					bb.varReg = bb.var.reg;
				}
			}
		}
	}

	private Random random = new Random();

	private void clear() {
		for (Register reg : general_regs) {
			if (reg.var != null) {
				reg.var = null;
			}
		}
		for (Register reg : float_regs) {
			if (reg.var != null) {
				reg.var = null;
			}
		}
	}

	private void bind(Register reg, Temp temp) {
		reg.var = temp;
		temp.reg = reg;
	}

	private void findReg(Tac tac, Temp temp, boolean read) {
		// already in reg
		if (temp.reg != null) {
			if (temp.equals(temp.reg.var)) {
				return;
			}
		}

		// find a reg do not need to spill
		if (temp.isFloat()) {
			for (Register reg : float_regs) {
				if (reg.var == null || !isAlive(tac, reg.var)) {
					bind(reg, temp);
					if (read) {
						load(tac, temp);
					}
					return;
				}
			}
		} else {
			for (Register reg : general_regs) {
				if (reg.var == null || !isAlive(tac, reg.var)) {
					bind(reg, temp);
					if (read) {
						load(tac, temp);
					}
					return;
				}
			}
		}
		

		// find a reg which var's offset already fixed to spill
		if (temp.isFloat()) {
			for (Register reg : float_regs) {
				if (reg.var.isOffsetFixed()) {
					spill(tac, reg.var);
					bind(reg, temp);
					if (read) {
						load(tac, temp);
					}
					return;
				}
			}
		} else {
			for (Register reg : general_regs) {
				if (reg.var.isOffsetFixed()) {
					spill(tac, reg.var);
					bind(reg, temp);
					if (read) {
						load(tac, temp);
					}
					return;
				}
			}
		}
		

		// random select a reg to spill
		Register reg;
		if (temp.isFloat()) {
			reg = float_regs[random.nextInt(float_regs.length)];
		} else {
			reg = general_regs[random.nextInt(general_regs.length)];
		}
		frameManager.findSlot(reg.var);
		spill(tac, reg.var);
		bind(reg, temp);
		if (read) {
			load(tac, temp);
		}
	}

	private void findRegForRead(Tac tac, Temp temp) {
		findReg(tac, temp, true);
	}

	private void spill(Tac tac, Temp temp) {
		Tac spill = Tac.genStore(temp, fp, Temp.createConstTemp(temp.offset));
		bb.insertBefore(spill, tac);
	}

	private void load(Tac tac, Temp temp) {
		if (!temp.isOffsetFixed()) {
			Driver.getDriver()
					.getOption()
					.getErr()
					.println( temp + " may used before define during register allocation");
			frameManager.findSlot(temp);
		}
		Tac load = Tac.genLoad(temp, fp, Temp.createConstTemp(temp.offset));
		bb.insertBefore(load, tac);
	}

	private boolean isAlive(Tac tac, Temp temp) {
		if (tac != null && tac.prev != null) {
			tac = tac.prev;
			while (tac != null && tac.liveOut == null) {
				tac = tac.prev;
			}
			if (tac != null) {
				return tac.liveOut.contains(temp);
			}
		}
		return bb.liveIn.contains(temp);
	}

	private void findRegForWrite(Tac tac, Temp temp) {
		findReg(tac, temp, false);
	}

}
