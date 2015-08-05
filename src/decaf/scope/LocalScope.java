package decaf.scope;

import decaf.tree.Tree;
import decaf.tree.Tree.Block;
import decaf.tree.Tree.Switch;
import decaf.symbol.Symbol;
import decaf.utils.IndentPrintWriter;

public class LocalScope extends Scope {

	private Tree node;

	public LocalScope(Tree node) {
		this.node = node;
	}

	@Override
	public Kind getKind() {
		return Kind.LOCAL;
	}

	@Override
	public void printTo(IndentPrintWriter pw) {
		pw.println("LOCAL SCOPE:");
		pw.incIndent();
		for (Symbol symbol : symbols.values()) {
			pw.println(symbol);
		}
		if (node.tag == Tree.TAG.BLOCK) {
			Block bl = (Block) node;
			for (Tree s : bl.block) {
				if (s.tag == Tree.TAG.BLOCK) {
					((Block) s).associatedScope.printTo(pw);
				} else if (s.tag == Tree.TAG.SWITCH) {
					((Switch) s).associatedScope.printTo(pw);
				}
			}
		} else if (node.tag == Tree.TAG.SWITCH) {
			Switch sw = (Switch) node;
			for (Tree.CaseStmt cs : sw.branches) {
				for (Tree s : cs.stmts) {
					if (s.tag == Tree.TAG.BLOCK) {
						((Block) s).associatedScope.printTo(pw);
					} else if (s.tag == Tree.TAG.SWITCH) {
						((Switch) s).associatedScope.printTo(pw);
					}
				}
			}
		}
		
		pw.decIndent();
	}

	@Override
	public boolean isLocalScope() {
		return true;
	}
}
