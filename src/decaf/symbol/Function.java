package decaf.symbol;

import decaf.Driver;
import decaf.Location;
import decaf.tree.Tree.Block;
import decaf.scope.FormalScope;
import decaf.scope.Scope;
import decaf.tac.Functy;
import decaf.type.FuncType;
import decaf.type.Type;

public class Function extends Symbol {

	private FormalScope associatedScope;

	private Functy functy;
	
	private boolean isMain;
	
	private boolean isDeclare;

	public boolean isMain() {
		return isMain;
	}

	public void setMain(boolean isMain) {
		this.isMain = isMain;
	}
	
	public boolean isDeclare() {
		return isDeclare;
	}
	
	public void setDeclare(boolean isDeclare) {
		this.isDeclare = isDeclare;
	}

	private int offset;

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Functy getFuncty() {
		return functy;
	}

	public void setFuncty(Functy functy) {
		this.functy = functy;
	}

	public Function(String name, Type returnType,
			Block node, boolean isDeclare, Location location) {
		this.name = name;
		this.location = location;
		this.isDeclare = isDeclare;

		type = new FuncType(returnType);
		associatedScope = new FormalScope(this, node);
	}

	public FormalScope getAssociatedScope() {
		return associatedScope;
	}

	public Type getReturnType() {
		return getType().getReturnType();
	}

	public void appendParam(Variable arg) {
		arg.setOrder(getType().numOfParams());
		getType().appendParam(arg.getType());
	}

	@Override
	public Scope getScope() {
		return definedIn;
	}

	@Override
	public FuncType getType() {
		return (FuncType) type;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public String toString() {
		return location + " -> " + "function " + name + " : " + type;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

}
