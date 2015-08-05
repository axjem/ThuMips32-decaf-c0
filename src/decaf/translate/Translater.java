package decaf.translate;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import decaf.Driver;
import decaf.tree.Tree;
import decaf.backend.OffsetCounter;
import decaf.error.RuntimeError;
import decaf.machdesc.Intrinsic;
import decaf.symbol.Function;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.tac.Functy;
import decaf.tac.Label;
import decaf.tac.Tac;
import decaf.tac.Temp;
import decaf.type.BaseType;
import decaf.type.Type;

public class Translater {
	private List<Functy> funcs;

	private Functy currentFuncty;
	
	private Tac global_head;
	
	private Tac global_tail;
	
	public int global_size;
	
	public Variable global_addr;

	public Translater(Variable global_addr) {
//		vtables = new ArrayList<VTable>();
		funcs = new ArrayList<Functy>();
		this.global_addr = global_addr;
	}

	public static Translater translate(Tree.TopLevel tree) {
		Translater tr = new Translater((Variable) Driver.getDriver().getTable().lookforGlobalAddr());
		TransPass1 tp1 = new TransPass1(tr);
		tp1.visitTopLevel(tree);
		TransPass2 tp2 = new TransPass2(tr);
		tp2.visitTopLevel(tree);
		return tr;
	}

	public void printTo(PrintWriter pw) {
		pw.println(Driver.getDriver().getOption().getGlobalName() + ": size " + global_size);
		pw.println();
		for (Functy ft : funcs) {
			pw.println("FUNCTION(" + ft.label.name + ") {");
			pw.println(ft.paramMemo);
			Tac tac = ft.head;
			while (tac != null) {
				if (tac.opc == Tac.Kind.MARK) {
					pw.println(tac);//generate main(or other label):
				} else {
					pw.println("    " + tac);
				}
				tac = tac.next;
			}
			pw.println("}");
			pw.println();
		}
	}

	public List<Functy> getFuncs() {
		return funcs;
	}

	public void createFuncty(Function func) {
		Functy functy = new Functy();
		if (func.isMain()) {
			functy.label = Label.createLabel("main", true);
		} else {
			functy.label = Label.createLabel("_" + func.getName(), true);
		}
		functy.sym = func;
		func.setFuncty(functy);
	}

	public void beginFunc(Function func) {
		currentFuncty = func.getFuncty();
		currentFuncty.paramMemo = memoOf(func);
		genMark(func.getFuncty().label);
		genLoadGlobalAddr(global_addr.getTemp(), Driver.getDriver().getOption().getGlobalName());
		if (currentFuncty.label.name.equals("main")) {
			Tac pTac = global_head;
			while (pTac != null) {
				append(pTac);
				pTac = pTac.next;
			}
		}
	}

	public void endFunc() {
		funcs.add(currentFuncty);
		currentFuncty = null;
	}
	
	public int sizeof(Type type) {
		if (type.equal(BaseType.CHAR())) {
			return OffsetCounter.BYTE_SIZE;
		} else {
			return OffsetCounter.WORD_SIZE;
		}
	}

	private Tac memoOf(Function func) {
		StringBuilder sb = new StringBuilder();
		Iterator<Symbol> iter = func.getAssociatedScope().iterator();
		while (iter.hasNext()) {
			Variable v = (Variable) iter.next();
			Temp t = v.getTemp();
			t.offset = v.getOffset();
			sb.append(t.name + ":" + t.offset + " ");
		}
		if (sb.length() > 0) {
			return Tac.genMemo(sb.substring(0, sb.length() - 1));
		} else {
			return Tac.genMemo("");
		}
	}

	public void append(Tac tac) {
		if (currentFuncty == null) {
			if (global_head == null) {
				global_head = global_tail = tac;
			} else {
				tac.prev = global_tail;
				global_tail.next = tac;
				global_tail = tac;
			}
		} else {
			if (currentFuncty.head == null) {
				currentFuncty.head = currentFuncty.tail = tac;
			} else {
				tac.prev = currentFuncty.tail;
				currentFuncty.tail.next = tac;
				currentFuncty.tail = tac;
			}
		}
	}
	
	public void genLoadGlobalAddr(Temp global, String name) {
		append(Tac.genLoadGlobalAddr(global, name));
	}

	public Temp genAdd(Temp src1, Temp src2) {
		Temp dst;
		if (src1.isFloat()) {
			dst = Temp.createTempF4();
		} else {
			dst = Temp.createTempI4();
		}
		append(Tac.genAdd(dst, src1, src2));
		return dst;
	}
	
	public Temp genSub(Temp src1, Temp src2) {
		Temp dst;
		if (src1.isFloat()) {
			dst = Temp.createTempF4();
		} else {
			dst = Temp.createTempI4();
		}
		append(Tac.genSub(dst, src1, src2));
		return dst;
	}

	public Temp genMul(Temp src1, Temp src2) {
		Temp dst;
		if (src1.isFloat()) {
			dst = Temp.createTempF4();
		} else {
			dst = Temp.createTempI4();
		}
		append(Tac.genMul(dst, src1, src2));
		return dst;
	}

	public Temp genDiv(Temp src1, Temp src2) {
		Temp dst;
		if (src1.isFloat()) {
			dst = Temp.createTempF4();
		} else {
			dst = Temp.createTempI4();
		}
		append(Tac.genDiv(dst, src1, src2));
		return dst;
	}

	public Temp genMod(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genMod(dst, src1, src2));
		return dst;
	}

	public Temp genNeg(Temp src) {
		Temp dst;
		if (src.isFloat()) {
			dst = Temp.createTempF4();
		} else {
			dst = Temp.createTempI4();
		}
		append(Tac.genNeg(dst, src));
		return dst;
	}
	
	public Temp genLAnd(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genLAnd(dst, src1, src2));
		return dst;
	}

	public Temp genLOr(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genLOr(dst, src1, src2));
		return dst;
	}

	public Temp genLNot(Temp src) {
		Temp dst = Temp.createTempI4();
		append(Tac.genLNot(dst, src));
		return dst;
	}

	public Temp genGtr(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genGtr(dst, src1, src2));
		return dst;
	}

	public Temp genGeq(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genGeq(dst, src1, src2));
		return dst;
	}

	public Temp genEqu(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genEqu(dst, src1, src2));
		return dst;
	}

	public Temp genNeq(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genNeq(dst, src1, src2));
		return dst;
	}

	public Temp genLeq(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genLeq(dst, src1, src2));
		return dst;
	}

	public Temp genLes(Temp src1, Temp src2) {
		Temp dst = Temp.createTempI4();
		append(Tac.genLes(dst, src1, src2));
		return dst;
	}
	
	public Temp genTypeCast(Temp src, Type fromType, Type toType) {
		if (fromType.equal(toType))
			return src;
		if (toType.equal(BaseType.FLOAT())) {
			Temp dst = genMoveToFloat(src);
			append(Tac.genConvToFloat(dst, dst));
			return dst;
//		} else if (toType.equal(BaseType.BOOL()) && fromType.equal(BaseType.FLOAT())) {
//			Temp dst = Temp.createTempI4();
//			Label load0 = Label.createLabel();
//			Label over = Label.createLabel();
//			genBeqz_f(src, load0);
//			genAssign(dst, genLoadImm4(1));
//			genBranch(over);
//			genMark(load0);
//			genAssign(dst, genLoadImm4(0));
//			genMark(over);
//			return dst;
//		} else if (toType.equal(BaseType.BOOL()) && !fromType.equal(BaseType.FLOAT())) {
//			Temp dst = Temp.createTempI4();
//			Label load0 = Label.createLabel();
//			Label over = Label.createLabel();
//			genBeqz(src, load0);
//			genAssign(dst, genLoadImm4(1));
//			genBranch(over);
//			genMark(load0);
//			genAssign(dst, genLoadImm4(0));
//			genMark(over);
//			return dst;
		} else if (fromType.equal(BaseType.FLOAT())) {
			Temp mid = Temp.createTempF4();
			append(Tac.genConvToInt(mid, src));
			Temp dst = genMoveToInt(mid);
			return dst;
		}
		return src;
	}

	public void genAssign(Temp dst, Temp src) {
		append(Tac.genAssign(dst, src));
	}

	public Temp genIndirectCall(Temp func, Type retType) {
		Temp dst;
		if (retType.equal(BaseType.VOID())) {
			dst = null;
		} else {
			dst = Temp.createTemp(retType);
		}
		append(Tac.genIndirectCall(dst, func));
		return dst;
	}

	public Temp genDirectCall(Label func, Type retType) {
		Temp dst;
		if (retType.equal(BaseType.VOID())) {
			dst = null;
		} else {
			dst = Temp.createTemp(retType);
		}
		append(Tac.genDirectCall(dst, func));
		return dst;
	}

	public Temp genIntrinsicCall(Intrinsic intrn) {
		Temp dst;
		if (intrn.type.equal(BaseType.VOID())) {
			dst = null;
		} else {
			dst = Temp.createTemp(intrn.type);
		}
		append(Tac.genDirectCall(dst, intrn.label));
		return dst;
	}

	public void genReturn(Temp src) {
		append(Tac.genReturn(src));
	}

	public void genBranch(Label dst) {
		append(Tac.genBranch(dst));
	}
	
	public Temp genMoveToInt(Temp src) {
		Temp dst = Temp.createTempI4();
		append(Tac.genMoveToInt(dst, src));
		return dst;
	}
	
	public Temp genMoveToFloat(Temp src) {
		Temp dst = Temp.createTempF4();
		append(Tac.genMoveToFloat(dst, src));
		return dst;
	}

	public void genBeqz(Temp cond, Label dst) {
		if (cond.isFloat()) {
			Temp i = genMoveToInt(cond);
			append(Tac.genBeqz(i, dst));
		} else {
			append(Tac.genBeqz(cond, dst));
		}
	}
	
	public void genBnez(Temp cond, Label dst) {
		if (cond.isFloat()) {
			Temp i = genMoveToInt(cond);
			append(Tac.genBnez(i, dst));
		} else {
			append(Tac.genBnez(cond, dst));
		}
	}

	public Temp genLoad(Temp base, int offset, Type type) {
		Temp dst = Temp.createTemp(type);
		append(Tac.genLoad(dst, base, Temp.createConstTemp(offset)));
		return dst;
	}
	
	public Temp genLoad_b(Temp base, int offset, Type type) {
		Temp dst = Temp.createTemp(type);
		append(Tac.genLoad_b(dst, base, Temp.createConstTemp(offset)));
		return dst;
	}

	public void genStore(Temp src, Temp base, int offset) {
		append(Tac.genStore(src, base, Temp.createConstTemp(offset)));
	}
	
	public void genStore_b(Temp src, Temp base, int offset) {
		append(Tac.genStore_b(src, base, Temp.createConstTemp(offset)));
	}

	public Temp genLoadImm4(int imm) {
		Temp dst = Temp.createTempI4();
		append(Tac.genLoadImm4(dst, Temp.createConstTemp(imm)));
		return dst;
	}
	
	public Temp genLoadFmm4(float imm) {
		Temp dst = Temp.createTempF4();
		append(Tac.genLoadFmm4(dst, Temp.createConstTemp_f(imm)));
		return dst;
	}

	public Temp genLoadStrConst(String value) {
		Temp dst = Temp.createTempI4();
		append(Tac.genLoadStrConst(dst, value));
		return dst;
	}

	public void genMemo(String comment) {
		append(Tac.genMemo(comment));
	}

	public void genMark(Label label) {
		append(Tac.genMark(label));
	}

	public void genParm(Temp parm) {
		append(Tac.genParm(parm));
	}

	public void genCheckArrayIndex(Temp index) {
		Temp cond = genLes(index, genLoadImm4(0));
		Label exit = Label.createLabel();
		genBeqz(cond, exit);
		Temp msg = genLoadStrConst(RuntimeError.ARRAY_INDEX_OUT_OF_BOUND);
		genParm(msg);
		genIntrinsicCall(Intrinsic.PRINT_STRING);
		genIntrinsicCall(Intrinsic.HALT);
		genMark(exit);
	}

	public void genCheckAllocSize(Temp size) {
		Label exit = Label.createLabel();
		Temp cond = genLes(size, genLoadImm4(0));
		genBeqz(cond, exit);
		Temp msg = genLoadStrConst(RuntimeError.NEGATIVE_ARR_SIZE);
		genParm(msg);
		genIntrinsicCall(Intrinsic.PRINT_STRING);
		genIntrinsicCall(Intrinsic.HALT);
		genMark(exit);
	}
	
	public Temp genAlloc(Temp length) {
		genCheckAllocSize(length);
		genParm(length);
		Temp obj = genIntrinsicCall(Intrinsic.ALLOCATE);
		return obj;
	}
	
	public Temp genNewArray(Temp length, Temp unit) {
//		Temp size = genAdd(unit, genMul(unit, length));
		Temp size = genMul(unit, length);
		genCheckAllocSize(size);
		genParm(size);
		Temp obj = genIntrinsicCall(Intrinsic.ALLOCATE);
		return obj;
	}
	
}
