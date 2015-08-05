package decaf.translate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import decaf.Driver;
import decaf.tree.Tree;
import decaf.type.BaseType;
import decaf.type.Type;
import decaf.backend.OffsetCounter;
import decaf.symbol.Function;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.tac.Temp;

public class TransPass1 extends Tree.Visitor {
	private Translater tr;

	private int objectSize;

	private List<Variable> vars;

	public TransPass1(Translater tr) {
		this.tr = tr;
		vars = new ArrayList<Variable>();
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		Temp t = Temp.createTemp(program.global_addr.getType());
		t.sym = program.global_addr;
		program.global_addr.setTemp(t);
		t.offset = -2 * OffsetCounter.WORD_SIZE;
		
		vars.clear();
		objectSize = 0;
		for (Tree.ControlLine cl: program.controlList) {
			cl.accept(this);
		}
		for (Tree fl: program.fieldList){
			fl.accept(this);
		}
		OffsetCounter oc = OffsetCounter.GLOBAL_OFFSET_COUNTER;
		for (Variable v: vars) {
			v.setOffset(oc.next(OffsetCounter.WORD_SIZE));
			objectSize += OffsetCounter.WORD_SIZE;
		}
		tr.global_size = objectSize;
	}

	@Override
	public void visitMethodDef(Tree.MethodDef funcDef) {
		Function func = funcDef.symbol;
		tr.createFuncty(func);
		OffsetCounter oc = OffsetCounter.PARAMETER_OFFSET_COUNTER;
		oc.reset();
		
		int order = 0;
		Iterator<Symbol> iter = func.getAssociatedScope().iterator();
		while (iter.hasNext()) {
			Variable v = (Variable) iter.next();
			v.setOrder(order++);
			Temp t = Temp.createTemp(v.getType());
			t.sym = v;
			t.isParam = true;
			v.setTemp(t);
			v.setOffset(oc.next(v.getTemp().size));
		}
	}
	
	@Override
	public void visitControlLine(Tree.ControlLine ctrlLine) {
		vars.add(ctrlLine.symbol);
	}
	
	@Override
	public void visitConDef(Tree.ConDef conDef) {
		for (Tree.InitVar iv: conDef.vars) {
			vars.add(iv.symbol);
		}
	}

	@Override
	public void visitVarDef(Tree.VarDef varDef) {
		for (Tree.VarComp vc: varDef.vars) {
			vars.add(vc.symbol);
		}
	}

}
