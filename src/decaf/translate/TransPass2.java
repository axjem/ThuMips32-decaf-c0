package decaf.translate;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import decaf.Driver;
import decaf.tree.Tree;
import decaf.backend.OffsetCounter;
import decaf.machdesc.Intrinsic;
import decaf.symbol.Function;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.tac.Label;
import decaf.tac.Temp;
import decaf.type.BaseType;
import decaf.type.PointerType;
import decaf.type.Type;

public class TransPass2 extends Tree.Visitor {

	private Translater tr;

//	private Temp currentThis;

	private Stack<Label> loopExits;

	public TransPass2(Translater tr) {
		this.tr = tr;
		loopExits = new Stack<Label>();
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		for (Tree.ControlLine cl: program.controlList) {
			cl.accept(this);
		}
		for (Tree fl: program.fieldList) {
			fl.accept(this);
		}
	}
	
	@Override
	public void visitControlLine(Tree.ControlLine ctrlLine) {
		ctrlLine.expr.accept(this);
		Temp trans = tr.genTypeCast(ctrlLine.expr.val, ctrlLine.expr.type, ctrlLine.symbol.getType());
		if (ctrlLine.symbol.getType().equal(BaseType.CHAR())) {
			tr.genStore_b(trans, tr.global_addr.getTemp(), ctrlLine.symbol.getOffset());
		} else {
			tr.genStore(trans, tr.global_addr.getTemp(), ctrlLine.symbol.getOffset());
		}
	}
	
	@Override
	public void visitConDef(Tree.ConDef conDef) {
		for (Tree.InitVar iv : conDef.vars) {
			iv.expr.accept(this);
			Temp trans = tr.genTypeCast(iv.expr.val, iv.expr.type, iv.symbol.getType());
			if (iv.symbol.getScope().isGlobalScope()) {
				if (iv.symbol.getType().equal(BaseType.CHAR())) {
					tr.genStore_b(trans, tr.global_addr.getTemp(), iv.symbol.getOffset());
				} else {
					tr.genStore(trans, tr.global_addr.getTemp(), iv.symbol.getOffset());
				}
			} else {
				Temp t = Temp.createTemp(iv.symbol.getType());
				t.sym = iv.symbol;
				iv.symbol.setTemp(t);
				tr.genAssign(iv.symbol.getTemp(), trans);
			}
			
		}
	}

	@Override
	public void visitVarDef(Tree.VarDef varDef) {
		for (Tree.VarComp vc : varDef.vars) {
			if (!vc.symbol.getScope().isGlobalScope()) {
				Temp t = Temp.createTemp(vc.symbol.getType());
				t.sym = vc.symbol;
				vc.symbol.setTemp(t);
			}
			if (vc.range != null) {
				vc.range.accept(this);
				Temp baseSize = tr.genLoadImm4(tr.sizeof(((PointerType) vc.symbol.getType()).getTargetType()));
				Temp addr = tr.genNewArray(vc.range.val, baseSize);
				if (vc.symbol.getScope().isGlobalScope()) {
					if (vc.symbol.getType().equal(BaseType.CHAR())) {
						tr.genStore_b(addr, tr.global_addr.getTemp(), vc.symbol.getOffset());
					} else {
						tr.genStore(addr, tr.global_addr.getTemp(), vc.symbol.getOffset());
					}
				} else {
					tr.genAssign(vc.symbol.getTemp(), addr);
				}
			}
			if (vc.expr != null) {
				vc.expr.accept(this);
				Temp trans = tr.genTypeCast(vc.expr.val, vc.expr.type, vc.symbol.getType());
				if (vc.symbol.getScope().isGlobalScope()) {
					if (vc.symbol.getType().equal(BaseType.CHAR())) {
						tr.genStore_b(trans, tr.global_addr.getTemp(), vc.symbol.getOffset());
					} else {
						tr.genStore(trans, tr.global_addr.getTemp(), vc.symbol.getOffset());
					}
				} else {
					tr.genAssign(vc.symbol.getTemp(), trans);
				}
			}
		}
	}
	
	@Override
	public void visitMethodDef(Tree.MethodDef funcDefn) {
		tr.beginFunc(funcDefn.symbol);
		funcDefn.body.accept(this);
		tr.endFunc();
//		currentThis = null;
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.left.accept(this);
		expr.right.accept(this);
		switch (expr.tag) {
		case PLUS:
			if (expr.left.type.isPointerType()) {
				expr.val = tr.genAdd(expr.left.val, tr.genMul(expr.right.val, tr.genLoadImm4(tr.sizeof(((PointerType) expr.left.type).getTargetType()))));
			} else if (expr.right.type.isPointerType()) {
				expr.val = tr.genAdd(tr.genMul(expr.left.val, tr.genLoadImm4(tr.sizeof(((PointerType) expr.right.type).getTargetType()))), expr.right.val);
			} else {
				if (expr.type.equal(BaseType.FLOAT())) {
					Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, expr.type);
					Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, expr.type);
					expr.val = tr.genAdd(trans1, trans2);
				} else {
					expr.val = tr.genAdd(expr.left.val, expr.right.val);
				}
			}
			break;
		case MINUS:
			if (expr.left.type.isPointerType()) {
				expr.val = tr.genSub(expr.left.val, tr.genMul(expr.right.val, tr.genLoadImm4(tr.sizeof(((PointerType) expr.left.type).getTargetType()))));
			} else {
				if (expr.type.equal(BaseType.FLOAT())) {
					Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, expr.type);
					Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, expr.type);
					expr.val = tr.genSub(trans1, trans2);
				} else {
					expr.val = tr.genSub(expr.left.val, expr.right.val);
				}
			}
			break;
		case MUL:
			if (expr.type.equal(BaseType.FLOAT())) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, expr.type);
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, expr.type);
				expr.val = tr.genMul(trans1, trans2);
			} else {
				expr.val = tr.genMul(expr.left.val, expr.right.val);
			}
			break;
		case DIV:
			if (expr.type.equal(BaseType.FLOAT())) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, expr.type);
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, expr.type);
				expr.val = tr.genDiv(trans1, trans2);
			} else {
				expr.val = tr.genDiv(expr.left.val, expr.right.val);
			}
			break;
		case MOD:
			expr.val = tr.genMod(expr.left.val, expr.right.val);
			break;
		case AND:
			expr.val = tr.genLAnd(expr.left.val, expr.right.val);
			break;
		case OR:
			expr.val = tr.genLOr(expr.left.val, expr.right.val);
			break;
		case LT:
			if (BaseType.FLOAT().equal(expr.left.type) || BaseType.FLOAT().equal(expr.right.type)) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, BaseType.FLOAT());
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, BaseType.FLOAT());
				expr.val = tr.genLes(trans1, trans2);
			} else {
				expr.val = tr.genLes(expr.left.val, expr.right.val);
			}
			break;
		case LE:
			if (BaseType.FLOAT().equal(expr.left.type) || BaseType.FLOAT().equal(expr.right.type)) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, BaseType.FLOAT());
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, BaseType.FLOAT());
				expr.val = tr.genLeq(trans1, trans2);
			} else {
				expr.val = tr.genLeq(expr.left.val, expr.right.val);
			}
			break;
		case GT:
			if (BaseType.FLOAT().equal(expr.left.type) || BaseType.FLOAT().equal(expr.right.type)) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, BaseType.FLOAT());
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, BaseType.FLOAT());
				expr.val = tr.genGtr(trans1, trans2);
			} else {
				expr.val = tr.genGtr(expr.left.val, expr.right.val);
			}
			break;
		case GE:
			if (BaseType.FLOAT().equal(expr.left.type) || BaseType.FLOAT().equal(expr.right.type)) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, BaseType.FLOAT());
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, BaseType.FLOAT());
				expr.val = tr.genGeq(trans1, trans2);
			} else {
				expr.val = tr.genGeq(expr.left.val, expr.right.val);
			}
			break;
		case EQ:
			if (BaseType.FLOAT().equal(expr.left.type) || BaseType.FLOAT().equal(expr.right.type)) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, BaseType.FLOAT());
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, BaseType.FLOAT());
				expr.val = tr.genEqu(trans1, trans2);
			} else {
				expr.val = tr.genEqu(expr.left.val, expr.right.val);
			}
			break;
		case NE:
			if (BaseType.FLOAT().equal(expr.left.type) || BaseType.FLOAT().equal(expr.right.type)) {
				Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, BaseType.FLOAT());
				Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, BaseType.FLOAT());
				expr.val = tr.genNeq(trans1, trans2);
			} else {
				expr.val = tr.genNeq(expr.left.val, expr.right.val);
			}
			break;
		case ASSIGN:
			Temp trans = tr.genTypeCast(expr.right.val, expr.right.type, expr.type);
			expr.val = trans;
			visitAssign((Tree.LValue)expr.left, trans);
			break;
		case AE:
			if (expr.left.type.isPointerType()) {
				expr.val = tr.genAdd(expr.left.val, tr.genMul(expr.right.val, tr.genLoadImm4(tr.sizeof(((PointerType) expr.left.type).getTargetType()))));
			} else {
				if (expr.type.equal(BaseType.FLOAT())) {
					Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, expr.type);
					Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, expr.type);
					expr.val = tr.genAdd(trans1, trans2);
				} else {
					expr.val = tr.genAdd(expr.left.val, expr.right.val);
				}
//				expr.val = tr.genAdd(expr.left.val, expr.right.val);
			}
			visitAssign((Tree.LValue)expr.left, expr.val);
			break;
		case SE:
			if (expr.left.type.isPointerType()) {
				expr.val = tr.genSub(expr.left.val, tr.genMul(expr.right.val, tr.genLoadImm4(tr.sizeof(((PointerType) expr.left.type).getTargetType()))));
			} else {
				if (expr.type.equal(BaseType.FLOAT())) {
					Temp trans1 = tr.genTypeCast(expr.left.val, expr.left.type, expr.type);
					Temp trans2 = tr.genTypeCast(expr.right.val, expr.right.type, expr.type);
					expr.val = tr.genSub(trans1, trans2);
				} else {
					expr.val = tr.genSub(expr.left.val, expr.right.val);
				}
			}
			visitAssign((Tree.LValue)expr.left, expr.val);
			break;
		}
	}

	public void visitAssign(Tree.LValue lvalue, Temp right) {
		Temp esz;
		Temp t;
		Temp base;
		switch (lvalue.lvKind) {
		case ARRAY_ELEMENT:
			Tree.Indexed arrayRef = (Tree.Indexed)lvalue;
			int unit = tr.sizeof(((PointerType) arrayRef.pointer.type).getTargetType());
			esz = tr.genLoadImm4(unit);
			if (arrayRef.index != null) {
				t = tr.genMul(arrayRef.index.val, esz);
			} else {
				t = tr.genMul(tr.genLoadImm4(0), esz);
			}
			base = tr.genAdd(arrayRef.pointer.val, t);
			if (lvalue.type.equal(BaseType.CHAR())) {
				tr.genStore_b(right, base, 0);
			} else {
				tr.genStore(right, base, 0);
			}
			break;
//		case MEMBER_VAR:
//			varRef = (Tree.Ident)lvalue;
//			tr.genStore(right, varRef.owner.val, varRef.symbol.getOffset());
//			break;
//		case POINTER_MEMBER_VAR:
//			varRef = (Tree.Ident)lvalue;
//			base = tr.genLoad(varRef.owner.val, 0);
//			tr.genStore(right, base, varRef.symbol.getOffset());
//			break;
		case GLOBAL_VAR:
			Tree.Ident varRef = (Tree.Ident)lvalue;
			if (varRef.type.equal(BaseType.CHAR())) {
				tr.genStore_b(right, tr.global_addr.getTemp(), varRef.symbol.getOffset());
			} else {
				tr.genStore(right, tr.global_addr.getTemp(), varRef.symbol.getOffset());
			}
			break;
		case PARAM_VAR:
		case LOCAL_VAR:
			tr.genAssign(lvalue.val, right);
			break;
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.FLOAT:
			literal.val = tr.genLoadFmm4((Float)literal.value);
			break;
		case Tree.INT:
			literal.val = tr.genLoadImm4(((Integer)literal.value).intValue());
			break;
		case Tree.BOOL:
			literal.val = tr.genLoadImm4((Boolean)(literal.value) ? 1 : 0);
			break;
		case Tree.CHAR:
			literal.val = tr.genLoadImm4(((String)(literal.value)).charAt(0));
			break;
		default:
			literal.val = tr.genLoadStrConst((String)literal.value);
		}
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		Temp t = tr.genLoadImm4(1);
		switch (expr.tag){
		case GETADDR:
//			expr.val = tr.genAddress((Tree.LValue)expr.expr);
			break;
		case SUF_SA:
			expr.val = expr.expr.val;
			if (expr.expr.type.isPointerType()) {
				t = tr.genAdd(expr.expr.val, tr.genMul(t, tr.genLoadImm4(tr.sizeof(((PointerType) expr.expr.type).getTargetType()))));
			} else {
				t = tr.genAdd(expr.expr.val, t);
			}
			visitAssign((Tree.LValue)expr.expr, t);
			break;
		case SUF_SS:
			expr.val = expr.expr.val;
			if (expr.expr.type.isPointerType()) {
				t = tr.genSub(expr.expr.val, tr.genMul(t, tr.genLoadImm4(tr.sizeof(((PointerType) expr.expr.type).getTargetType()))));
			} else {
				t = tr.genSub(expr.expr.val ,t);
			}
			visitAssign((Tree.LValue)expr.expr, t);
			break;
		case PRE_SA:
			if (expr.expr.type.isPointerType()) {
				expr.val = tr.genAdd(expr.expr.val, tr.genMul(t, tr.genLoadImm4(tr.sizeof(((PointerType) expr.expr.type).getTargetType()))));
			} else {
				expr.val = tr.genAdd(expr.expr.val, t);
			}
			visitAssign((Tree.LValue)expr.expr, expr.val);
			break;
		case PRE_SS:
			if (expr.expr.type.isPointerType()) {
				expr.val = tr.genSub(expr.expr.val, tr.genMul(t, tr.genLoadImm4(tr.sizeof(((PointerType) expr.expr.type).getTargetType()))));
			} else {
				expr.val = tr.genSub(expr.expr.val ,t);
			}
			visitAssign((Tree.LValue)expr.expr, expr.val);
			break;
		case NEG:
			expr.val = tr.genNeg(expr.expr.val);
			break;
		case POS:
			expr.val = expr.expr.val;
			break;
		case NOT:
			expr.val = tr.genLNot(expr.expr.val);
			break;
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.val = tr.genLoadImm4(0);
	}

	@Override
	public void visitBlock(Tree.Block block) {
		for (Tree s : block.block) {
			s.accept(this);
		}
	}

//	@Override
//	public void visitThisExpr(Tree.ThisExpr thisExpr) {
//		thisExpr.val = currentThis;
//	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
			Temp trans = tr.genTypeCast(returnStmt.expr.val, returnStmt.expr.type, returnStmt.returnType);
			tr.genReturn(trans);//TODO:float move to $v0 reg 
		} else {
			tr.genReturn(null);
		}
	}

	@Override
	public void visitPrintf(Tree.Printf printfStmt) {
		for (Tree.Expr r : printfStmt.exprs) {
			r.accept(this);
			tr.genParm(r.val);
			if (r.type.equal(BaseType.BOOL())) {
				tr.genIntrinsicCall(Intrinsic.PRINT_BOOL);
			} else if (r.type.equal(BaseType.INT())) {
				tr.genIntrinsicCall(Intrinsic.PRINT_INT);
			} else if (r.type.equal(BaseType.CHAR())) {
				tr.genIntrinsicCall(Intrinsic.PRINT_CHAR);
			} else if (r.type.equal(BaseType.FLOAT())) {
				tr.genIntrinsicCall(Intrinsic.PRINT_FLOAT);
			} else if (r.type.equal(new PointerType(BaseType.CHAR()))) {
				tr.genIntrinsicCall(Intrinsic.PRINT_STRING);
			}
		}
	}
	
	@Override
	public void visitScanf(Tree.Scanf scanfStmt) {//TODO
		for (Tree.Expr r : scanfStmt.exprs) {
			r.accept(this);
			Temp res = null;
			if (r.type.equal(BaseType.INT())) {
				res = tr.genIntrinsicCall(Intrinsic.READ_INT);
			} else if (r.type.equal(BaseType.CHAR())) {
				res = tr.genIntrinsicCall(Intrinsic.READ_CHAR);
			} else if (r.type.equal(BaseType.FLOAT())) {
				res = tr.genIntrinsicCall(Intrinsic.READ_FLOAT);
			} else if (r.type.equal(new PointerType(BaseType.CHAR()))) {
				res = tr.genIntrinsicCall(Intrinsic.READ_LINE);
			}
			if (res != null) {
				visitAssign((Tree.LValue) r, res);
			}
		}
	}
	
//	@Override
//	public void visitReadIntExpr(Tree.ReadIntExpr readIntExpr) {
//		readIntExpr.val = tr.genIntrinsicCall(Intrinsic.READ_INT);
//	}
//
//	@Override
//	public void visitReadLineExpr(Tree.ReadLineExpr readStringExpr) {
//		Temp size = tr.genLoadImm4(256);
//		tr.genParm(size);
//		Temp str = tr.genIntrinsicCall(Intrinsic.ALLOCATE);
//		Label loop = Label.createLabel();
//		Label exit = Label.createLabel();
//		Temp end = tr.genLoadImm4('\n');
//		Temp index = tr.genLoadImm4(0);
//		Temp esz = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
//		Temp step = tr.genLoadImm4(1);
//		tr.genMark(loop);
//		Temp val = tr.genIntrinsicCall(Intrinsic.READ_CHAR);
//		Temp cond = tr.genNeq(val, end);
//		tr.genBeqz(cond, exit);
//		Temp base = tr.genAdd(str, tr.genMul(index, esz));
//		tr.genStore(val, base, 0);
//		tr.genAssign(index, tr.genAdd(index, step));
//		tr.genBranch(loop);
//		tr.genMark(exit);
//		base = tr.genAdd(str, tr.genMul(index, esz));
//		tr.genStore(tr.genLoadImm4('\0'), base, 0);
//
//		readStringExpr.val = str;
//	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.pointer.accept(this);
		Temp base;
		if (indexed.index == null) {
			base = indexed.pointer.val;
		} else {
			indexed.index.accept(this);
			tr.genCheckArrayIndex(indexed.index.val);
			
			Temp esz = tr.genLoadImm4(tr.sizeof(((PointerType) indexed.pointer.type).getTargetType()));
			Temp t = tr.genMul(indexed.index.val, esz);
			base = tr.genAdd(indexed.pointer.val, t);
		}
		if (indexed.type.equal(BaseType.CHAR())) {
			indexed.val = tr.genLoad_b(base, 0, indexed.type);
		} else {
			indexed.val = tr.genLoad(base, 0, indexed.type);
		}
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
//		if(ident.lvKind == Tree.LValue.Kind.MEMBER_VAR 
//			|| ident.lvKind == Tree.LValue.Kind.POINTER_MEMBER_VAR) {
//			ident.owner.accept(this);
//		}
		
		switch (ident.lvKind) {
		case GLOBAL_VAR:
			if (ident.type.equal(BaseType.CHAR())) {
				ident.val = tr.genLoad_b(tr.global_addr.getTemp(), ident.symbol.getOffset(), ident.type);
			} else {
				ident.val = tr.genLoad(tr.global_addr.getTemp(), ident.symbol.getOffset(), ident.type);
			}
			break;
//		case MEMBER_VAR:
//			ident.val = tr.genLoad(ident.owner.val, ident.symbol.getOffset());
//			break;
//		case POINTER_MEMBER_VAR:
//			Temp base = tr.genLoad(ident.owner.val, 0);
//			ident.val = tr.genLoad(base, ident.symbol.getOffset());
//			break;
		default:
			ident.val = ident.symbol.getTemp();
			break;
		}
	}
	
	@Override
	public void visitBreak(Tree.Break breakStmt) {
		tr.genBranch(loopExits.peek());
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {//TODO: float reg as parameter
		Iterator<Type> iter = callExpr.symbol.getType().getArgList().iterator();
		for (Tree.Expr expr : callExpr.actuals) {
			expr.accept(this);
		}
		for (Tree.Expr expr : callExpr.actuals) {
			Temp trans = tr.genTypeCast(expr.val, expr.type, iter.next());
			tr.genParm(trans);
		}
		callExpr.val = tr.genDirectCall(callExpr.symbol.getFuncty().label, callExpr.symbol.getReturnType());//TODO: $v0 reg move to float reg if necessary
	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		Label cond = Label.createLabel();
		Label loop = Label.createLabel();
		tr.genBranch(cond);
		tr.genMark(loop);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		tr.genMark(cond);
		forLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(forLoop.condition.val, exit);
		loopExits.push(exit);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		ifStmt.condition.accept(this);
		if (ifStmt.falseBranch != null) {
			Label falseLabel = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, falseLabel);
			ifStmt.trueBranch.accept(this);
			Label exit = Label.createLabel();
			tr.genBranch(exit);
			tr.genMark(falseLabel);
			ifStmt.falseBranch.accept(this);
			tr.genMark(exit);
		} else if (ifStmt.trueBranch != null) {
			Label exit = Label.createLabel();
			tr.genBeqz(ifStmt.condition.val, exit);
			if (ifStmt.trueBranch != null) {
				ifStmt.trueBranch.accept(this);
			}
			tr.genMark(exit);
		}
	}
	
	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		Label loop = Label.createLabel();
		tr.genMark(loop);
		whileLoop.condition.accept(this);
		Label exit = Label.createLabel();
		tr.genBeqz(whileLoop.condition.val, exit);
		loopExits.push(exit);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		tr.genBranch(loop);
		loopExits.pop();
		tr.genMark(exit);
	}
	
	@Override
	public void visitSwitch(Tree.Switch switchStmt) {
		Label exit = Label.createLabel();
		switchStmt.expr.accept(this);
		loopExits.push(exit);
		Label skip = null;
		for (int i = 0; i < switchStmt.branches.size(); i++) {
			Tree.CaseStmt csStmt = switchStmt.branches.get(i);
			Label next = Label.createLabel();
			if (csStmt.condition != null) {
				csStmt.condition.accept(this);
				Temp cond = tr.genEqu(switchStmt.expr.val, csStmt.condition.val);
				tr.genBeqz(cond, next);
			}
			
			if (skip != null) {
				tr.genMark(skip);
			}
			for (Tree stmt : csStmt.stmts) {
				stmt.accept(this);
			}
			if (i != switchStmt.branches.size() - 1) {
				skip = Label.createLabel();
				tr.genBranch(skip);
			}
			tr.genMark(next);
		}
		loopExits.pop();
		tr.genMark(exit);
	}
	
	@Override
	public void visitSizeOf(Tree.SizeOf sizeOfExpr) {
		Temp size;
		if (sizeOfExpr.basic_type.type.equal(BaseType.CHAR())) {
			if (sizeOfExpr.pointer_num == 0) {
				size = tr.genLoadImm4(OffsetCounter.BYTE_SIZE);
			} else {
				size = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
			}
		} else {
			size = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
		}
		sizeOfExpr.val = size;
	}
	
	@Override
	public void visitAlloc(Tree.Alloc allocExpr) {
		allocExpr.expr.accept(this);
		allocExpr.val = tr.genAlloc(allocExpr.expr.val);
	}

//	@Override
//	public void visitNewExpr(Tree.NewExpr newExpr) {
//		Temp baseSize = tr.genLoadImm4(OffsetCounter.WORD_SIZE);
//		if (newExpr.length != null) {
//			newExpr.length.accept(this);
//			newExpr.val = tr.genNewExpr(newExpr.length.val, baseSize, newExpr.elementType.type);
//		} else {
//			newExpr.val = tr.genNewExpr(tr.genLoadImm4(1), baseSize, newExpr.elementType.type);
//		}
//	}

//	@Override
//	public void visitNewClass(Tree.NewClass newClass) {
//		newClass.val = tr.genDirectCall(newClass.symbol.getNewFuncLabel(),
//				BaseType.INT);
//	}

//	@Override
//	public void visitTypeTest(Tree.TypeTest typeTest) {
//		typeTest.instance.accept(this);
//		typeTest.val = tr.genInstanceof(typeTest.instance.val,
//				typeTest.symbol);
//	}

	@Override
	public void visitTypeCast(Tree.TypeCast typeCast) {
		typeCast.expr.accept(this);
//		if (!typeCast.expr.type.compatible(typeCast.symbol.getType())) {
//			tr.genClassCast(typeCast.expr.val, typeCast.symbol);
//		}
		typeCast.val = tr.genTypeCast(typeCast.expr.val, typeCast.expr.type, typeCast.type);
	}
}
