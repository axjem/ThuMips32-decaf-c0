package decaf.typecheck;

import java.util.Iterator;
import java.util.List;

import decaf.Driver;
import decaf.Location;
import decaf.tree.Tree;
import decaf.tree.Tree.FormalDef;
import decaf.error.BadInheritanceError;
import decaf.error.BadOverrideError;
import decaf.error.BadVoidTypeError;
import decaf.error.DecafError;
import decaf.error.DeclConflictError;
import decaf.error.NoMainFuncError;
import decaf.error.OverridingVarError;
import decaf.error.StructNotFoundError;
import decaf.scope.GlobalScope;
import decaf.scope.LocalScope;
import decaf.scope.ScopeStack;
import decaf.symbol.Function;
import decaf.symbol.Symbol;
import decaf.symbol.Variable;
import decaf.type.BaseType;
import decaf.type.FuncType;
import decaf.type.PointerType;
import decaf.type.Type;

public class BuildSym extends Tree.Visitor {

	private ScopeStack table;

	private void issueError(DecafError error) {
		Driver.getDriver().issueError(error);
	}

	public BuildSym(ScopeStack table) {
		this.table = table;
	}

	public static void buildSymbol(Tree.TopLevel tree) {
		new BuildSym(Driver.getDriver().getTable()).visitTopLevel(tree);
	}

	// root
	@Override
	public void visitTopLevel(Tree.TopLevel program) {
		program.globalScope = new GlobalScope();
		table.open(program.globalScope);
		Variable v = new Variable("", new PointerType(BaseType.VOID()), program.getLocation());
		program.global_addr = v;
		table.declare(v);
		
		for (Tree.ControlLine cl : program.controlList) {
			cl.accept(this);
		}
		
		for (Tree f: program.fieldList) {
			f.accept(this);
			if (f.tag == Tree.TAG.METHODDEF) {
				Tree.MethodDef mdef = (Tree.MethodDef)f;
				if(Driver.getDriver().getOption().getMainFuncName().equals(mdef.name))
					program.main = mdef.symbol;
			}
		}

		if (!isMainFunc(program.main)) {
			issueError(new NoMainFuncError(Driver.getDriver().getOption().getMainFuncName()));
		}
		table.close();
	}
	
	@Override
	public void visitControlLine(Tree.ControlLine cl) {
		cl.expr.accept(this);
		cl.expr.type.setConst(true);
		Variable v = new Variable(cl.name, cl.expr.type, cl.getLocation());
		Symbol sym = table.lookup(cl.name, false);
		if (sym != null) {
			issueError(new DeclConflictError(v.getLocation(), v.getName(), sym.getLocation()));
		} else {
			table.declare(v);
		}
		cl.symbol = v;
	}

	// visiting declarations
//	@Override
//	public void visitStructDeclare(Tree.StructDeclare structDcl) {
//		Struct s = new Struct(structDcl.name, true, structDcl.loc);
//		s.createType();
//		Symbol sym = table.lookup(structDcl.name, true);
//		if (sym != null) {
//			issueError(new DeclConflictError(s.getLocation(), s.getName(), sym.getLocation()));
//		} else {
//			table.declare(s);
//		}
//		structDcl.symbol = s;
//	}
//	
//	@Override
//	public void visitStructDef(Tree.StructDef structDef) {
//		Struct s = new Struct(structDef.name, false, structDef.loc);
//		s.createType();
//		Symbol sym = table.lookup(structDef.name, true);
//		if (sym != null) {
//			if (sym.isStruct() && ((Struct)sym).isDeclare()) {
//				s = (Struct)sym;
//				s.setDeclare(false);
//			} else {
//				issueError(new DeclConflictError(s.getLocation(), s.getName(), sym.getLocation()));
//			}
//		} else {
//			table.declare(s);
//		}
//		structDef.symbol = s;
//		
//		table.open(s.getAssociatedScope());
//		for (Tree.VarDef f : structDef.fields) {
//			f.accept(this);
//		}
//		table.close();
//	}
	
	@Override
	public void visitConDef(Tree.ConDef conDef) {
		conDef.type.accept(this);
		conDef.type.type.setConst(true);
		boolean e = false;
		if (conDef.type.type.equal(BaseType.VOID())) {
			e = true;
			issueError(new BadVoidTypeError(conDef.getLocation()));
		}
		
		for (Tree.InitVar iv : conDef.vars) {
			Variable v;
			if (e == true) {
				v = new Variable(iv.name, BaseType.ERROR(), iv.getLocation());
			} else {
				v = new Variable(iv.name, conDef.type.type, iv.getLocation());
			}
			Symbol sym = table.lookup(iv.name, false);
			if (sym != null) {
				issueError(new DeclConflictError(v.getLocation(), v.getName(), sym.getLocation()));
			} else {
				table.declare(v);
			}
			iv.symbol = v;
		}
	}

	@Override
	public void visitVarDef(Tree.VarDef varDef) {
		varDef.type.accept(this);
		for (Tree.VarComp vc : varDef.vars) {
			Type t = varDef.type.type;
			for (int i = 0; i < vc.pointer_num; i++) {
				t = new decaf.type.PointerType(t);
			}
			if (vc.range != null) {
				t = new decaf.type.PointerType(t);
			}
			if (t.equal(BaseType.VOID())) {
				issueError(new BadVoidTypeError(vc.getLocation()));
			}
			Variable v = new Variable(vc.name, t, vc.getLocation());
			Symbol sym = table.lookup(vc.name, false);
			if (sym != null) {
				issueError(new DeclConflictError(v.getLocation(), v.getName(), sym.getLocation()));
			} else {
				table.declare(v);
			}
			vc.symbol = v;
		}
	}
	
	@Override
	public void visitFormalDef(Tree.FormalDef fdef) {
		fdef.type.accept(this);
		Type t = fdef.type.type;
		for (int i = 0; i < fdef.pointer_num; i++) {
			t = new decaf.type.PointerType(t);
		}
		if (t.equal(BaseType.VOID())) {
			issueError(new BadVoidTypeError(fdef.getLocation()));
		}
		Variable v = new Variable(fdef.name, t, fdef.getLocation());
		Symbol sym = table.lookup(fdef.name, false);
		if (sym != null) {
			issueError(new DeclConflictError(v.getLocation(), v.getName(), sym.getLocation()));
		} else {
			table.declare(v);
		}
		fdef.symbol = v;
	}
	
	@Override
	public void visitMethodDeclare(Tree.MethodDeclare funcDcl) {
		funcDcl.returnType.accept(this);
		Type t = funcDcl.returnType.type;
		for (int i = 0; i < funcDcl.pointer_num; i++) {
			t = new decaf.type.PointerType(t);
		}
		Function f = new Function(funcDcl.name, t, null, true, funcDcl.getLocation());
		Symbol sym = table.lookup(funcDcl.name, false);
		if (sym != null) {
			issueError(new DeclConflictError(funcDcl.getLocation(), funcDcl.name, sym.getLocation()));
		} else {
			table.declare(f);
		}
		funcDcl.symbol = f;
		
		table.open(f.getAssociatedScope());
		for (Tree.FormalDef d : funcDcl.formals) {
			d.accept(this);
			f.appendParam(d.symbol);
		}
		table.close();
	}

	@Override
	public void visitMethodDef(Tree.MethodDef funcDef) {
		funcDef.returnType.accept(this);
		Type t = funcDef.returnType.type;
		for (int i = 0; i < funcDef.pointer_num; i++) {
			t = new decaf.type.PointerType(t);
		}
		Function f = new Function(funcDef.name,	t, funcDef.body, false, funcDef.getLocation());
		Symbol sym = table.lookup(funcDef.name, false);
		table.open(f.getAssociatedScope());
		for (Tree.FormalDef d : funcDef.formals) {
			d.accept(this);
			f.appendParam(d.symbol);
		}
		table.close();
		
		if (sym != null) {
			if (sym.isFunction() && ((Function)sym).isDeclare()
				&& ((Function)sym).getType().equal(f.getType())) {
				f = (Function)sym;
			} else {
				issueError(new DeclConflictError(funcDef.getLocation(), funcDef.name, sym.getLocation()));
			}
		} else {
			table.declare(f);
		}
		
		funcDef.symbol = f;
		table.open(f.getAssociatedScope());
		funcDef.body.accept(this);
		table.close();
	}

	// visiting types
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
	public void visitLiteral(Tree.Literal literal) {
		switch (literal.typeTag) {
		case Tree.INT:
			literal.type = BaseType.INT();
			break;
		case Tree.BOOL:
			literal.type = BaseType.BOOL();
			break;
		case Tree.FLOAT:
			literal.type = BaseType.FLOAT();
			break;
		case Tree.STRING:
			literal.type = new decaf.type.PointerType(BaseType.CHAR());
			break;
		case Tree.CHAR:
			literal.type = BaseType.CHAR();
			break;
		}
	}

	@Override
	public void visitNull(Tree.Null nullExpr) {
		nullExpr.type = BaseType.NULL();
	}

//	@Override
//	public void visitTypeStruct(Tree.TypeStruct typeStruct) {
//		Struct c = table.lookupStruct(typeStruct.name);
//		if (c == null) {
//			issueError(new StructNotFoundError(typeStruct.getLocation(),
//					typeStruct.name));
//			typeStruct.type = BaseType.ERROR;
//		} else {
//			typeStruct.type = c.getType();
//		}
//	}

//	@Override
//	public void visitTypePointer(Tree.TypePointer typePointer) {
//		typePointer.targetType.accept(this);
//		if (typePointer.targetType.type.equal(BaseType.ERROR)) {
//			typePointer.type = BaseType.ERROR;
//		} else if (typePointer.targetType.type.equal(BaseType.VOID)) {
//			issueError(new BadArrElementError(typePointer.getLocation()));
//			typePointer.type = BaseType.ERROR;
//		} else {
//			typePointer.type = new decaf.type.PointerType(typePointer.targetType.type);
//		}
//	}

	// for VarDecl in LocalScope
	@Override
	public void visitBlock(Tree.Block block) {
		block.associatedScope = new LocalScope(block);
		table.open(block.associatedScope);
		for (Tree s : block.block) {
			s.accept(this);
		}
		table.close();
	}

	@Override
	public void visitForLoop(Tree.ForLoop forLoop) {
		if (forLoop.loopBody != null) {
			forLoop.loopBody.accept(this);
		}
	}

	@Override
	public void visitIf(Tree.If ifStmt) {
		if (ifStmt.trueBranch != null) {
			ifStmt.trueBranch.accept(this);
		}
		if (ifStmt.falseBranch != null) {
			ifStmt.falseBranch.accept(this);
		}
	}

	@Override
	public void visitWhileLoop(Tree.WhileLoop whileLoop) {
		if (whileLoop.loopBody != null) {
			whileLoop.loopBody.accept(this);
		}
	}
	
	@Override
	public void visitSwitch(Tree.Switch sw) {
		sw.associatedScope = new LocalScope(sw);
		table.open(sw.associatedScope);
		for (Tree.CaseStmt cs : sw.branches) {
			cs.accept(this);
		}
		table.close();
	}
	
	@Override
	public void visitCaseStmt(Tree.CaseStmt cs) {
		for (Tree s : cs.stmts) {
			s.accept(this);
		}
	}

//	private int calcOrder(Class c) {
//		if (c == null) {
//			return -1;
//		}
//		if (c.getOrder() < 0) {
//			c.setOrder(0);
//			c.setOrder(calcOrder(c.getParent()) + 1);
//		}
//		return c.getOrder();
//	}

	private boolean isMainFunc(Function f) {
		if (f == null) {
			return false;
		}
		f.setMain(true);
		FuncType type = f.getType();
		return type.getReturnType().equal(BaseType.VOID()) && type.numOfParams() == 0;
	}
}
