package decaf.typecheck;

import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import decaf.Driver;
import decaf.Location;
import decaf.tree.Tree;
import decaf.error.BadArgCountError;
import decaf.error.BadArgError;
import decaf.error.BadArgTypeError;
import decaf.error.BadArrElementError;
import decaf.error.BadLengthArgError;
import decaf.error.BadLengthError;
import decaf.error.BadNewArrayLength;
import decaf.error.BadNewElementError;
import decaf.error.BadReturnTypeError;
import decaf.error.BadSwitchCondition;
import decaf.error.BadTestExpr;
import decaf.error.BadVoidTypeError;
import decaf.error.BreakOutOfLoopError;
import decaf.error.ClassNotFoundError;
import decaf.error.DecafError;
import decaf.error.FieldNotAccessError;
import decaf.error.FieldNotFoundError;
import decaf.error.IncompatBinOpError;
import decaf.error.IncompatInitError;
import decaf.error.IncompatUnOpError;
import decaf.error.MethodNotFoundError;
import decaf.error.NotArrayError;
import decaf.error.NotClassError;
import decaf.error.NotClassFieldError;
import decaf.error.NotClassMethodError;
import decaf.error.NotIntError;
import decaf.error.NotMethodError;
import decaf.error.NotPointerError;
import decaf.error.NotVarError;
import decaf.error.RefNonStaticError;
import decaf.error.StructNotFoundError;
import decaf.error.ThisInStaticFuncError;
import decaf.error.TypeCastError;
import decaf.error.UndeclVarError;
import decaf.error.UnexpectedType;
import decaf.frontend.Parser;
import decaf.scope.FormalScope;
import decaf.scope.GlobalScope;
import decaf.scope.Scope;
import decaf.scope.ScopeStack;
import decaf.scope.Scope.Kind;
import decaf.symbol.Function;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.type.*;

public class TypeCheck extends Tree.Visitor {

	private ScopeStack table;

	private Stack<Tree> breaks;

	private Function currentFunction;

	public TypeCheck(ScopeStack table) {
		this.table = table;
		breaks = new Stack<Tree>();
	}

	public static void checkType(Tree.TopLevel tree) {
		new TypeCheck(Driver.getDriver().getTable()).visitTopLevel(tree);
	}

	@Override
	public void visitBinary(Tree.Binary expr) {
		expr.type = checkBinaryOp(expr.left, expr.right, expr.tag, expr.loc);
	}

	@Override
	public void visitUnary(Tree.Unary expr) {
		expr.expr.accept(this);
		if (expr.expr.type.equal(BaseType.ERROR())) {
			expr.type = expr.expr.type;
		} else {
			switch (expr.tag) {
//			case GETADDR:
//				expr.type = new PointerType(expr.expr.type);
//				break;
			case SUF_SA:
			case SUF_SS:
			case PRE_SA:
			case PRE_SS:
				if (BaseType.INT().compatible(expr.expr.type)) {
					expr.type = BaseType.INT();
				} else if (expr.expr.type.isPointerType()) {
					expr.type = expr.expr.type;
				} else {
					issueError(new IncompatUnOpError(expr.getLocation(), "++/--", expr.expr.type.toString()));
					expr.type = BaseType.ERROR();
				}
				break;
			case POS:
			case NEG:
				if (BaseType.INT().compatible(expr.expr.type)) {
					expr.type = BaseType.INT();
				} else if (BaseType.FLOAT().compatible(expr.expr.type)) {
					expr.type = BaseType.FLOAT();
				} else {
					issueError(new IncompatUnOpError(expr.getLocation(), "-/+", expr.expr.type.toString()));
					expr.type = BaseType.ERROR();
				}
				break;
			case NOT:
				if (BaseType.BOOL().compatible(expr.expr.type)) {
					expr.type = BaseType.BOOL();
				} else {
					issueError(new IncompatUnOpError(expr.getLocation(), "!", expr.expr.type.toString()));
					expr.type = BaseType.ERROR();
				}
				break;
			}
		}
	}

	@Override
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.type = BaseType.INT();
			break;
		case Tree.FLOAT:
			literal.type = BaseType.FLOAT();
			break;
		case Tree.BOOL:
			literal.type = BaseType.BOOL();
			break;
		case Tree.STRING:
			literal.type = new PointerType(BaseType.CHAR());
			break;
		case Tree.CHAR:
			literal.type = BaseType.CHAR();
			break;
		}
	}
	
	@Override
	public void visitTypeIdent(Tree.TypeIdent type) {
		switch (type.typeTag) {
		case Tree.FLOAT:
			type.type = BaseType.FLOAT();
			break;
		case Tree.VOID:
			type.type = BaseType.VOID();
			break;
		case Tree.INT:
			type.type = BaseType.INT();
			break;
		case Tree.BOOL:
			type.type = BaseType.BOOL();
			break;
		default:
			type.type = BaseType.CHAR();
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.type = BaseType.NULL();
	}

	@Override
	public void visitIndexed(Tree.Indexed indexed) {
		indexed.lvKind = Tree.LValue.Kind.ARRAY_ELEMENT;
		indexed.pointer.accept(this);
		if (indexed.pointer.type.equal(BaseType.ERROR())) {
			indexed.type = BaseType.ERROR();
		} else if (!indexed.pointer.type.isPointerType()) {
			issueError(new NotPointerError(indexed.pointer.getLocation()));
			indexed.type = BaseType.ERROR();
		} else {
			indexed.type = ((PointerType) indexed.pointer.type).getTargetType();
			if ((indexed.pointer.type).isConst()) {
				indexed.type.setConst(true);
			}
		}
		if (indexed.index != null) {
			indexed.index.accept(this);
			if (!BaseType.INT().compatible(indexed.index.type)) {
				issueError(new NotIntError(indexed.getLocation()));
			}
		}
	}

	private void checkCallExpr(Tree.CallExpr callExpr, Symbol f) {
		if (f == null) {
			issueError(new MethodNotFoundError(callExpr.getLocation(), callExpr.method));
			callExpr.type = BaseType.ERROR();
		} else if (!f.isFunction()) {
			issueError(new NotMethodError(callExpr.getLocation(), callExpr.method));
			callExpr.type = BaseType.ERROR();
		} else {
			Function func = (Function) f;
			callExpr.symbol = func;
			callExpr.type = func.getReturnType();
			for (Tree.Expr e : callExpr.actuals) {
				e.accept(this);
			}
			List<Type> argList = func.getType().getArgList();
			int argCount = callExpr.actuals.size();
			if (argList.size() != argCount) {
				issueError(new BadArgCountError(callExpr.getLocation(),
							callExpr.method, argList.size(), callExpr.actuals.size()));
			} else {
				Iterator<Type> iter1 = argList.iterator();
				Iterator<Tree.Expr> iter2 = callExpr.actuals.iterator();
				for (int i = 1; iter1.hasNext(); i++) {
					Type t1 = iter1.next();
					Tree.Expr e = iter2.next();
					Type t2 = e.type;
					if (!t1.compatible(t2)) {
						issueError(new BadArgTypeError(e.getLocation(), i, 
								t2.toString(), t1.toString()));
					}
				}
			}
		}
	}

	@Override
	public void visitCallExpr(Tree.CallExpr callExpr) {
		checkCallExpr(callExpr, table.lookupBeforeLocation(callExpr.method, callExpr.getLocation()));
		return;
	}
	
	@Override
	public void visitAlloc(Tree.Alloc al) {
		al.expr.accept(this);
		al.type = new PointerType(BaseType.VOID());
		if (!BaseType.INT().compatible(al.expr.type)) {
			issueError(new NotIntError(al.getLocation()));
		}
	}
	
	@Override
	public void visitSizeOf(Tree.SizeOf so) {
		so.basic_type.accept(this);
		so.type = BaseType.INT();
	}
	
//	@Override
//	public void visitNewExpr(Tree.NewExpr newExpr) {
//		newExpr.elementType.accept(this);
//		if (newExpr.elementType.type.equal(BaseType.ERROR)) {
//			newExpr.type = BaseType.ERROR;
//		} else if (newExpr.elementType.type.equal(BaseType.VOID)) {
//			issueError(new BadArrElementError(newExpr.elementType.getLocation()));
//			newExpr.type = BaseType.ERROR;
//		} else {
//			newExpr.type = new PointerType(newExpr.elementType.type);
//			if (newExpr.length != null) {
//				newExpr.length.accept(this);
//				if (!newExpr.length.type.equal(BaseType.ERROR)
//					&& !newExpr.length.type.equal(BaseType.INT)) {
//					issueError(new BadNewArrayLength(newExpr.length.getLocation()));
//				}
//			}
//		}
//	}

//	@Override
//	public void visitNewClass(Tree.NewClass newClass) {
//		Class c = table.lookupClass(newClass.className);
//		newClass.symbol = c;
//		if (c == null) {
//			issueError(new ClassNotFoundError(newClass.getLocation(),
//					newClass.className));
//			newClass.type = BaseType.ERROR;
//		} else {
//			newClass.type = c.getType();
//		}
//	}

	@Override
	public void visitTypeCast(Tree.TypeCast cast) {
		cast.basic_type.accept(this);
		Type t = cast.basic_type.type;
		for (int i = 0; i < cast.pointer_num; i++) {
			t = new PointerType(t);
		}
		cast.expr.accept(this);
//		if (!t.compatible(cast.expr.type)) {
//			issueError(new TypeCastError(cast.expr.type.toString(), t.toString(), cast.expr.getLocation()));
//			t = BaseType.ERROR;
//		}
		cast.type = t;
	}

	@Override
	public void visitIdent(Tree.Ident ident) {
		Symbol v = table.lookupBeforeLocation(ident.name, ident.getLocation());
		if (v == null) {
			issueError(new UndeclVarError(ident.getLocation(), ident.name));
			ident.type = BaseType.ERROR();
		} else if (v.isVariable()) {
			Variable var = (Variable) v;
			ident.type = var.getType();
			ident.symbol = var;
			if (var.isLocalVar()) {
				ident.lvKind = Tree.LValue.Kind.LOCAL_VAR;
			} else if (var.isParam()) {
				ident.lvKind = Tree.LValue.Kind.PARAM_VAR;
			} else {
				ident.lvKind = Tree.LValue.Kind.GLOBAL_VAR;
			}
		} else {
			issueError(new NotVarError(ident.getLocation(), ident.name));
			ident.type = BaseType.ERROR();
		}
//		} else {
//			ident.owner.accept(this);
//			if (!ident.owner.type.equal(BaseType.ERROR)) {
//				if (!ident.isArrow ) {
//					if (ident.owner.type.isStructType()) {
//						StructScope ss = ((StructType)ident.owner.type).getStructScope();
//						Symbol v = ss.lookup(ident.name);
//						if (v == null) {
//							issueError(new FieldNotFoundError(ident.getLocation(), ident.name, ident.owner.type.toString()));
//							ident.type = BaseType.ERROR;
//						} else if (v.isVariable()) {
//							ident.symbol = (Variable)v;
//							ident.type = v.getType();
//							ident.lvKind = Tree.LValue.Kind.MEMBER_VAR;
//						} else {
//							issueError(new NotVarError(ident.getLocation(), ident.name));
//							ident.type = BaseType.ERROR;
//						}
//					} else {
//						issueError(new UnexpectedType("struct", ident.owner.type.toString(), ident.getLocation()));
//						ident.type = BaseType.ERROR;
//					}
//				} else {
//					if (ident.owner.type.isPointerType() 
//						&& ((PointerType)ident.owner.type).getTargetType().isStructType() ) {
//						StructType structtype = (StructType)((PointerType)ident.owner.type).getTargetType();
//						StructScope ss = structtype.getStructScope();
//						Symbol v = ss.lookup(ident.name);
//						if (v == null) {
//							issueError(new FieldNotFoundError(ident.getLocation(), ident.name, structtype.toString()));
//							ident.type = BaseType.ERROR;
//						} else if (v.isVariable()) {
//							ident.symbol = (Variable)v;
//							ident.type = v.getType();
//							ident.lvKind = Tree.LValue.Kind.POINTER_MEMBER_VAR;
//						} else {
//							issueError(new NotVarError(ident.getLocation(), ident.name));
//							ident.type = BaseType.ERROR;
//						}
//					} else {
//						issueError(new UnexpectedType("struct*", ident.owner.type.toString(), ident.getLocation()));
//						ident.type = BaseType.ERROR;
//					}
//				}
//			} else {
//				ident.type = BaseType.ERROR;
//			}
	}

//	@Override
//	public void visitStructDef(Tree.StructDef structDef) {
//		table.open(structDef.symbol.getAssociatedScope());
//		for (Tree.VarDef f : structDef.fields) {
//			f.accept(this);
//		}
//		table.close();
//	}
	
	@Override
	public void visitConDef(Tree.ConDef cdef) {
		for (Tree.InitVar iv : cdef.vars) {
			iv.expr.accept(this);
			if (!cdef.type.type.compatible(iv.expr.type)) {
				issueError(new IncompatInitError(iv.getLocation(), cdef.type.type.toString(), iv.expr.type.toString()));
			}
		}
	}
	
	@Override
	public void visitVarDef(Tree.VarDef vdef) {
		for (Tree.VarComp vc : vdef.vars) {
			if (vc.range != null) {
				vc.range.accept(this);
				if (!BaseType.INT().compatible(vc.range.type)) {
					issueError(new NotIntError(vc.range.getLocation()));
				}
			}
			if (vc.expr != null) {
				vc.expr.accept(this);
				if (!vc.symbol.getType().compatible(vc.expr.type)) {
					issueError(new IncompatInitError(vc.getLocation(), vc.symbol.getType().toString(), vc.expr.type.toString()));
				}
			}
		}
	}

	@Override
	public void visitMethodDef(Tree.MethodDef func) {
		this.currentFunction = func.symbol;
		table.open(func.symbol.getAssociatedScope());
		func.body.accept(this);
		table.close();
	}

	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		table.open(program.globalScope);
		for (Tree dcl : program.fieldList) {
			dcl.accept(this);
		}
		table.close();
	}

	@Override
	public void visitBlock(Tree.Block block) {
		table.open(block.associatedScope);
		for (Tree s : block.block) {
			s.accept(this);
		}
		table.close();
	}

//	@Override
//	public void visitAssign(Tree.Assign assign) {
//		assign.left.accept(this);
//		assign.expr.accept(this);
//		if (!assign.left.type.equal(BaseType.ERROR)
//				&& (assign.left.type.isFuncType() || !assign.expr.type
//						.compatible(assign.left.type))) {
//			issueError(new IncompatBinOpError(assign.getLocation(),
//					assign.left.type.toString(), "=", assign.expr.type
//							.toString()));
//		}
//	}

	@Override
	public void visitBreak(Tree.Break breakStmt) {
		if (breaks.empty()) {
			issueError(new BreakOutOfLoopError(breakStmt.getLocation()));
		}
	}
	
	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		checkTestExpr(whileLoop.condition);
		breaks.add(whileLoop);
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.init != null) {
			forLoop.init.accept(this);
		}
		checkTestExpr(forLoop.condition);
		if (forLoop.update != null) {
			forLoop.update.accept(this);
		}
		breaks.add(forLoop);
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
		breaks.pop();
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		checkTestExpr(ifStmt.condition);
		if (ifStmt.trueBranch != null) {
			ifStmt.trueBranch.accept(this);
		}
		if (ifStmt.falseBranch != null) {
			ifStmt.falseBranch.accept(this);
		}
	}
	
	@Override
	public void visitSwitch(Tree.Switch switchStmt) {
		table.open(switchStmt.associatedScope);
		switchStmt.expr.accept(this);
		if (!BaseType.INT().compatible(switchStmt.expr.type)) {
			issueError(new BadSwitchCondition(switchStmt.expr.getLocation()));
		}
		breaks.add(switchStmt);
		for (Tree.CaseStmt cs : switchStmt.branches) {
			cs.accept(this);
		}
		breaks.pop();
		table.close();
	}
	
	@Override
	public void visitCaseStmt(Tree.CaseStmt caseStmt) {
		if (caseStmt.condition != null) {
			caseStmt.condition.accept(this);
			if (!BaseType.INT().compatible(caseStmt.condition.type)) {
				issueError(new BadSwitchCondition(caseStmt.condition.getLocation()));
			}
		}
		for (Tree s : caseStmt.stmts) {
			s.accept(this);
		}
	}
	
	@Override
	public void visitScanf(Tree.Scanf scanfStmt) {
		int i = 0;
		for (Tree.Expr e : scanfStmt.exprs) {
			e.accept(this);
			i++;
			if (!e.type.equal(BaseType.ERROR())
				&& !e.type.equal(BaseType.INT())
				&& !e.type.equal(new PointerType(BaseType.CHAR()))
				&& !e.type.equal(BaseType.CHAR())
				&& !e.type.equal(BaseType.FLOAT())) {
				issueError(new BadArgError(e.getLocation(), Integer.toString(i), e.type.toString()));
			}
		}
	}

	@Override
	public void visitPrintf(Tree.Printf printfStmt) {
		int i = 0;
		for (Tree.Expr e : printfStmt.exprs) {
			e.accept(this);
			i++;
			if (!e.type.equal(BaseType.ERROR()) && !e.type.equal(BaseType.BOOL())
				&& !e.type.equal(BaseType.INT())
				&& !e.type.equal(new PointerType(BaseType.CHAR()))
				&& !e.type.equal(BaseType.CHAR())
				&& !e.type.equal(BaseType.FLOAT())) {
				issueError(new BadArgError(e.getLocation(), Integer.toString(i), e.type.toString()));
			}
		}
	}

	@Override
	public void visitReturn(Tree.Return returnStmt) {
//		Type returnType = ((FormalScope) table.lookForScope(Scope.Kind.FORMAL)).getOwner().getReturnType();
		Type returnType = currentFunction.getReturnType();
		returnStmt.returnType = returnType;
		if (returnStmt.expr != null) {
			returnStmt.expr.accept(this);
		}
		if (returnType.equal(BaseType.VOID())) {
			if (returnStmt.expr != null) {
				issueError(new BadReturnTypeError(returnStmt.getLocation(), returnType.toString(), returnStmt.expr.type.toString()));
			}
		} else if (returnStmt.expr == null) {
			issueError(new BadReturnTypeError(returnStmt.getLocation(), returnType.toString(), "void"));
		} else if (!returnType.compatible(returnStmt.expr.type)) {
			issueError(new BadReturnTypeError(returnStmt.getLocation(), returnType.toString(), returnStmt.expr.type.toString()));
		}
	}

//	@Override
//	public void visitTypePointer(Tree.TypePointer typePointer) {
//		typePointer.targetType.accept(this);
//		if (typePointer.targetType.type.equal(BaseType.ERROR)) {
//			typePointer.type = BaseType.ERROR;
//		} else if (typePointer.targetType.type.equal(BaseType.VOID)) {
//			issueError(new BadArrElementError(typePointer.getLocation()));
//			typePointer.type = BaseType.ERROR;
//		} else {
//			typePointer.type = new PointerType(typePointer.targetType.type);
//		}
//	}

	private void issueError(DecafError error) {
		Driver.getDriver().issueError(error);
	}

	private Type checkBinaryOp(Tree.Expr left, Tree.Expr right, Tree.TAG op, Location location) {
		left.accept(this);
		right.accept(this);
		if (left.type.equal(BaseType.ERROR()) || right.type.equal(BaseType.ERROR())) {
			switch (op) {
			case ASSIGN:
			case AE:
			case SE:
			case PLUS:
			case MINUS:
			case MUL:
			case DIV:
				return BaseType.ERROR();
			case MOD:
				return BaseType.INT();
			default:
				return BaseType.BOOL();
			}
		}

		boolean compatible = false;
		Type returnType = BaseType.ERROR();
		switch (op) {
		case ASSIGN:
			compatible = left.type.compatible(right.type) && !left.type.isConst() && (!right.type.isPointerType() || (right.type.isPointerType() && !right.type.isConst()));
			returnType = left.type;
			break;
		case PLUS:
			compatible = (BaseType.INT().compatible(left.type) && right.type.isPointerType())
						|| (left.type.isPointerType() && BaseType.INT().compatible(right.type))
						|| (BaseType.FLOAT().compatible(left.type) && BaseType.FLOAT().compatible(right.type));
			if (left.type.isPointerType())
				returnType = left.type;
			else if (right.type.isPointerType())
				returnType = right.type;
			else if (left.type.equal(BaseType.FLOAT()) || right.type.equal(BaseType.FLOAT()))
				returnType = BaseType.FLOAT();
			else
				returnType = BaseType.INT();
			break;
		case MINUS:
			compatible = (left.type.isPointerType() && BaseType.INT().compatible(right.type))
						|| (BaseType.FLOAT().compatible(left.type) && BaseType.FLOAT().compatible(right.type));
			if (left.type.isPointerType())
				returnType = left.type;
			else if (left.type.equal(BaseType.FLOAT()) || right.type.equal(BaseType.FLOAT()))
				returnType = BaseType.FLOAT();
			else
				returnType = BaseType.INT();
			break;
		case AE:
		case SE:
			compatible = (!left.type.isPointerType() && left.type.compatible(right.type))
						|| (left.type.isPointerType() && BaseType.INT().compatible(right.type));
			compatible = compatible && !left.type.isConst();
			returnType = left.type;
			break;
		case MUL:
		case DIV:
			compatible = BaseType.FLOAT().compatible(left.type) && BaseType.FLOAT().compatible(right.type);
			if (left.type.equal(BaseType.FLOAT()) || right.type.equal(BaseType.FLOAT()))
				returnType = BaseType.FLOAT();
			else
				returnType = BaseType.INT();
			break;
		case EQ:
		case NE:
		case GT:
		case GE:
		case LT:
		case LE:
			compatible = BaseType.FLOAT().compatible(left.type) && BaseType.FLOAT().compatible(right.type);
			returnType = BaseType.BOOL();
			break;
		case MOD:
			compatible = BaseType.INT().compatible(left.type) && BaseType.INT().compatible(right.type);
			returnType = BaseType.INT();
			break;
		case AND:
		case OR:
			compatible = BaseType.BOOL().compatible(left.type) && BaseType.BOOL().compatible(right.type);
			returnType = BaseType.BOOL();
			break;
		default:
			break;
		}

		if (!compatible) {
			issueError(new IncompatBinOpError(location, left.type.toString(),
					Tree.Binary.opStr(op), right.type.toString()));
		}
		return returnType;
	}

	private void checkTestExpr(Tree.Expr expr) {
		expr.accept(this);
		if (!BaseType.BOOL().compatible(expr.type)) {
			issueError(new BadTestExpr(expr.getLocation()));
		}
	}

}
