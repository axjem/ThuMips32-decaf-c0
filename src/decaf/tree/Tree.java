/**
 * @(#)Tree.java	1.30 03/01/23
 *
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package decaf.tree;

import java.util.List;

import decaf.*;
import decaf.type.*;
import decaf.frontend.Parser;
import decaf.scope.*;
import decaf.symbol.*;
import decaf.tac.Temp;
import decaf.utils.IndentPrintWriter;
import decaf.utils.MiscUtils;


/**
 * Root class for abstract syntax tree nodes. It provides
 *  definitions for specific tree nodes as subclasses nested inside
 *  There are 40 such subclasses.
 *
 *  Each subclass is highly standardized.  It generally contains only tree
 *  fields for the syntactic subcomponents of the node.  Some classes that
 *  represent identifier uses or definitions also define a
 *  Symbol field that denotes the represented identifier.  Classes
 *  for non-local jumps also carry the jump target as a field.  The root
 *  class Tree itself defines fields for the tree's type and
 *  position.  No other fields are kept in a tree node; instead parameters
 *  are passed to methods accessing the node.
 *
 *  The only method defined in subclasses is `visit' which applies a
 *  given visitor to the tree. The actual tree processing is done by
 *  visitor classes in other packages. The abstract class
 *  Visitor, as well as an Factory interface for trees, are
 *  defined as inner classes in Tree.
 *  @see TreeMaker
 *  @see TreeInfo
 *  @see TreeTranslator
 *  @see Pretty
 */
public abstract class Tree {
	
	public enum TAG {
		/**
	     * Toplevel nodes, of type TopLevel, representing entire source files.
	     */
	    TOPLEVEL,
	    
	    /**
	     * Import clauses, of type Import.
	     */
	     IMPORT,
	     
	     /**
	      * Control line definitions, of type ControlLine.
	      */
	     CONTROLLINE,
	     
	     /**
	      * Method declaration, of type MethodDeclare.
	      */
	     METHODDECLARE, 
	     
	     /**
	      * Method definitions, of type MethodDef.
	      */
	     METHODDEF, 
	     
	     /**
	      * Constant variable definitions, of type ConDef.
	      */
	     CONDEF, 
	     
	     /**
	      * Init variables, of type InitVar.
	      */
	     INITVAR,
	     
	     /**
	      * Variable definitions, of type VarDef.
	      */
	     VARDEF,
	     
	     /**
	      * Variable complement, of type VarComp.
	      */
	     VARCOMP, 
	     
	     /**
	      * Formal define, of type FormalDef.
	      */
	     FORMALDEF,
	     
	     /**
	      * The no-op statement ";", of type Skip
	      */
	     SKIP,
	     
	     
	     /**
	      * Blocks, of type Block.
	      */
	     BLOCK,
	     
	     /**
	      * Do-while loops, of type DoLoop.
	      */
	     DOLOOP,
	     
	     /**
	      * While-loops, of type WhileLoop.
	      */
	     WHILELOOP,
	     
	     /**
	      * For-loops, of type ForLoop.
	      */
	     FORLOOP,
	     
	     /**
	      * Labelled statements, of type Labelled.
	      */
	     LABELLED,
	     
	     /**
	      * Switch statements, of type Switch.
	      */
	     SWITCH,
	     
	     /**
	      * Case parts in switch statements, of type Case.
	      */
	     CASESTMT,
	     
	     /**
	      * Synchronized statements, of type Synchonized.
	      */
	     SYNCHRONIZED,
	     
	     /**
	      * Try statements, of type Try.
	      */
	     TRY,
	     
	     /**
	      * Catch clauses in try statements, of type Catch.
	      */
	     CATCH,
	     
	     /**
	      * Conditional expressions, of type Conditional.
	      */
	     CONDEXPR,
	     
	     /**
	      * Conditional statements, of type If.
	      */
	     IF,
	     
	     /**
	      * Expression statements, of type Exec.
	      */
	     EXEC,
	     
	     /**
	      * Break statements, of type Break.
	      */
	     BREAK,
	     
	     /**
	      * Continue statements, of type Continue.
	      */
	     CONTINUE,
	     
	     /**
	      * Return statements, of type Return.
	      */
	     RETURN,
	     
	     /**
	      * Throw statements, of type Throw.
	      */
	     THROW,
	     
	     /**
	      * Assert statements, of type Assert.
	      */
	     ASSERT,
	     
	     /**
	      * Method invocation expressions, of type Apply.
	      */
	     APPLY,
	     
	     /**
	      * Class instance creation expressions, of type NewClass.
	      */
	     NEWCLASS,
	     
	     /**
	      * Array creation expressions, of type NewArray.
	      */
	     NEWARRAY,
	     
	     /**
	      * Array creation expressions, of type NewArray.
	      */
	     NEWEXPR,
	     
	     /**
	      * Parenthesized subexpressions, of type Parens.
	      */
	     PARENS,
	     
	     /**
	      * Assignment expressions, of type Assign(change to binary operator).
	      */
	     ASSIGN,
	     
	     /**
	      * Type cast expressions, of type TypeCast.
	      */
	     TYPECAST,
	     
	     /**
	      * Type test expressions, of type TypeTest.
	      */
	     TYPETEST,
	     
	     /**
	      * Indexed array expressions, of type Indexed.
	      */
	     INDEXED,
	     
	     /**
	      * Selections, of type Select.
	      */
	     SELECT,
	     
	     /**
	      * Simple identifiers, of type Ident.
	      */
	     IDENT,
	     
	     /**
	      * Literals, of type Literal.
	      */
	     LITERAL,
	     
	     /**
	      * Basic type identifiers, of type TypeIdent.
	      */
	     TYPEIDENT,
	     
	     /**
	      * Pointer types, of type TypePointer.
	      */
	     TYPEPOINTER,
	     
	     /**
	      * Array types, of type TypeArray.
	      */
	     TYPEARRAY,
	     
	     /**
	      * Parameterized types, of type TypeApply.
	      */
	     TYPEAPPLY,
	     
	     /**
	      * Formal type parameters, of type TypeParameter.
	      */
	     TYPEPARAMETER,
	     
	     /**
	      * Error trees, of type Erroneous.
	      */
	     ERRONEOUS,
	     
	     /**
	      * Unary operators, of type Unary.
	      */
	     GETADDR,
	     SUF_SA,
	     SUF_SS,
	     PRE_SA,
	     PRE_SS,
	     POS,
	     NEG,
	     NOT,
	     COMPL,
	     PREINC,
	     PREDEC,
	     POSTINC,
	     POSTDEC,
	     
	     /**
	      * unary operator for null reference checks, only used internally.
	      */
	     NULLCHK,
	     
	     /**
	      * Binary operators, of type Binary.
	      */
	     AE,
	     SE,
	     OR,
	     AND,
	     BITOR,
	     BITXOR,
	     BITAND,
	     EQ,
	     NE,
	     LT,
	     GT,
	     LE,
	     GE,
	     SL,
	     SR,
	     USR,
	     PLUS,
	     MINUS,
	     MUL,
	     DIV,
	     MOD,

	     NULL,
	     CALLEXPR,
	     THISEXPR,
	     READINTEXPR,
	     READLINEEXPR,
	     PRINTF,
	     SCANF,
	     ALLOC,
	     SIZEOF,
	}

    /**
     * Tags for Literal and TypeLiteral
     */
    public static final int VOID = 0; 
    public static final int INT = VOID + 1; 
    public static final int BOOL = INT + 1; 
    public static final int FLOAT = BOOL + 1;
    public static final int STRING = FLOAT + 1; 
    public static final int CHAR = STRING + 1;


    
    public Location loc;
    public Type type;
    public TAG tag;

    /**
     * Initialize tree with given tag.
     */
    public Tree(TAG tag, Location loc) {
        super();
        this.tag = tag;
        this.loc = loc;
    }

	public Location getLocation() {
		return loc;
	}

    /**
      * Set type field and return this tree.
      */
    public Tree setType(Type type) {
        this.type = type;
        return this;
    }

    /**
      * Visit this tree with a given visitor.
      */
    public void accept(Visitor v) {
        v.visitTree(this);
    }

	public abstract void printTo(IndentPrintWriter pw);

    public static class TopLevel extends Tree {

    	public List<ControlLine> controlList;
    	public List<Tree> fieldList;
    	public Function main;
    	public Variable global_addr;
		public GlobalScope globalScope;
		
		public TopLevel(List<ControlLine> controls, List<Tree> fieldList, Location loc) {
			super(TAG.TOPLEVEL, loc);
			this.controlList = controls;
			this.fieldList = fieldList;
		}

    	@Override
        public void accept(Visitor v) {
            v.visitTopLevel(this);
        }

		@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("program");
    		pw.incIndent();
    		for (Tree c : controlList) {
    			c.printTo(pw);
    		}
    		for (Tree f : fieldList) {
    			f.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }
    
    /**
     * Control line for every program.
     * Here only for "#define".
     */
    public static class ControlLine extends Tree {
    	public String name;
    	public Expr expr;
    	public Variable symbol;
    	public boolean isDefined;
    	
    	public ControlLine(String name, Expr expr, Location loc) {
    		super(TAG.CONTROLLINE, loc);
    		this.name = name;
    		this.expr = expr;
    	}
    	
    	@Override
    	public void accept(Visitor v) {
    		v.visitControlLine(this);
    	}
    	
    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("control line define "+name);
    		pw.incIndent();
    		expr.printTo(pw);
    		pw.decIndent();
    	}
    }
    
    /**
     * Function declare.
     */
    public static class MethodDeclare extends Tree {
    	
    	public String name;
    	public TypeLiteral returnType;
    	public int pointer_num;
    	public List<FormalDef> formals;
    	public Function symbol;
    	
    	public MethodDeclare(String name, TypeLiteral returnType, int pointer_num, 
    			List<FormalDef> formals, Location loc) {
    		super(TAG.METHODDECLARE, loc);
    		this.name = name;
    		this.returnType = returnType;
    		this.pointer_num = pointer_num;
    		this.formals = formals;
    	}
    	
    	@Override
    	public void accept(Visitor v) {
    		v.visitMethodDeclare(this);
    	}
    	
    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("declare: ");
    		pw.print("func " + name + " ");
    		returnType.printTo(pw);
    		for (int i = 0; i < pointer_num; i++) {
    			pw.print('*');
    		}
    		pw.println();
    		pw.incIndent();
    		pw.println("formals");
    		pw.incIndent();
    		for (FormalDef d : formals) {
    			d.printTo(pw);
    		}
    		pw.decIndent();
    		pw.decIndent();
    	}
    }

    /**
     * Function define.
     */
    public static class MethodDef extends Tree {
    
    	public String name;
    	public TypeLiteral returnType;
    	public int pointer_num;
    	public List<FormalDef> formals;
    	public Block body;
    	public Function symbol;
    	
        public MethodDef(String name, TypeLiteral returnType, int pointer_num, 
        		List<FormalDef> formals, Block body, Location loc) {
            super(TAG.METHODDEF, loc);
    		this.name = name;
    		this.returnType = returnType;
    		this.pointer_num = pointer_num;
    		this.formals = formals;
    		this.body = body;
        }

        @Override
        public void accept(Visitor v) {
            v.visitMethodDef(this);
        }
    	
    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("func " + name + " ");
    		returnType.printTo(pw);
    		for (int i = 0; i < pointer_num; i++) {
    			pw.print('*');
    		}
    		pw.println();
    		pw.incIndent();
    		pw.println("formals");
    		pw.incIndent();
    		for (FormalDef d : formals) {
    			d.printTo(pw);
    		}
    		pw.decIndent();
    		body.printTo(pw);
    		pw.decIndent();
    	}
    }
    
    /**
     * Constant variables define.
     */
    public static class ConDef extends Tree {

    	public List<InitVar> vars;
    	public TypeLiteral type;
    	
		public ConDef(List<InitVar> vars, TypeLiteral type, Location loc) {
	        super(TAG.CONDEF, loc);
	        this.vars = vars;
	        this.type = type;
        }

		@Override
        public void accept(Visitor v) {
            v.visitConDef(this);
        }
		
		@Override
        public void printTo(IndentPrintWriter pw) {
			pw.print("condef ");
			type.printTo(pw);
			pw.println();
			for (InitVar i : vars) {
				i.printTo(pw);
			}
        }
    	
    }
    
    /**
     * Initialize variables, especially for constant variables.
     */
    public static class InitVar extends Tree {
    	
    	public String name;
    	public Expr expr;
    	public Variable symbol;
    	
    	public InitVar(String name, Expr expr, Location loc) {
    		super(TAG.INITVAR, loc);
    		this.name = name;
    		this.expr = expr;
    	}
    	
    	@Override
        public void accept(Visitor v) {
            v.visitInitVar(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("init: " + name + " = ");
    		expr.printTo(pw);
    	}
    }

    /**
     * Variables define.
     */
    public static class VarDef extends Tree {
    	
    	public List<VarComp> vars;
    	public TypeLiteral type;

        public VarDef(List<VarComp> vars, TypeLiteral type, Location loc) {
            super(TAG.VARDEF, loc);
    		this.vars = vars;
    		this.type = type;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitVarDef(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("vardef ");
    		type.printTo(pw);
    		pw.println();
    		pw.incIndent();
    		for (VarComp v : vars) {
    			v.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }
    
    /**
     * Complement of variables including assignment or array declare.
     */
    public static class VarComp extends Tree {
    	
    	public int pointer_num;
    	public String name;
    	public Expr range;
    	public Expr expr;
    	public Variable symbol;
    	
    	public VarComp(String name, int pointer_num, Expr range, Expr expr, Location loc) {
    		super(TAG.VARCOMP, loc);
    		this.pointer_num = pointer_num;
    		this.name = name;
    		this.range = range;
    		this.expr = expr;
    	}
    	
    	@Override
        public void accept(Visitor v) {
            v.visitVarComp(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		for (int i = 0; i < pointer_num; i++) {
    			pw.print('*');
    		}
    		pw.print(" "+name);
    		if (range != null) {
    			pw.print(" range: ");
    			range.printTo(pw);
    		} else {
    			pw.println();
    		}
    		if (expr != null) {
    			pw.print(" init: ");
    			expr.printTo(pw);
    		}
    	}
    }
    
    /**
     * Formals define for function define.
     */
    public static class FormalDef extends Tree {
    	
    	public String name;
    	public TypeLiteral type;
    	public int pointer_num;
    	public Variable symbol;

        public FormalDef(String name, int pointer_num, TypeLiteral type, Location loc) {
            super(TAG.FORMALDEF, loc);
    		this.name = name;
    		this.pointer_num = pointer_num;
    		this.type = type;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitFormalDef(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("formaldef ");
    		type.printTo(pw);
    		for (int i = 0; i < pointer_num; i++) {
    			pw.print('*');
    		}
    		pw.print(" "+name);
    		pw.println();
    	}
    }

    /**
      * A no-op statement ";".
      */
    public static class Skip extends Tree {

        public Skip(Location loc) {
            super(TAG.SKIP, loc);
        }

    	@Override
        public void accept(Visitor v) {
            v.visitSkip(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		//print nothing
    	}
    }

    public static class Block extends Tree {

    	public List<Tree> block;
    	public LocalScope associatedScope;

        public Block(List<Tree> block, Location loc) {
            super(TAG.BLOCK, loc);
    		this.block = block;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitBlock(this);
        }
    	
    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("stmtblock");
    		pw.incIndent();
    		for (Tree s : block) {
    			s.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }

    /**
      * A while loop
      */
    public static class WhileLoop extends Tree {

    	public Expr condition;
    	public Tree loopBody;

        public WhileLoop(Expr condition, Tree loopBody, Location loc) {
            super(TAG.WHILELOOP, loc);
            this.condition = condition;
            this.loopBody = loopBody;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitWhileLoop(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("while");
    		pw.incIndent();
    		condition.printTo(pw);
    		if (loopBody != null) {
    			loopBody.printTo(pw);
    		}
    		pw.decIndent();
    	}
   }

    /**
      * A for loop.
      */
    public static class ForLoop extends Tree {

    	public Tree init;
    	public Expr condition;
    	public Tree update;
    	public Tree loopBody;

        public ForLoop(Tree init, Expr condition, Tree update,
        		Tree loopBody, Location loc) {
            super(TAG.FORLOOP, loc);
    		this.init = init;
    		this.condition = condition;
    		this.update = update;
    		this.loopBody = loopBody;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitForLoop(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("for");
    		pw.incIndent();
    		if (init != null) {
    			init.printTo(pw);
    		} else {
    			pw.println("<emtpy>");
    		}
    		condition.printTo(pw);
    		if (update != null) {
    			update.printTo(pw);
    		} else {
    			pw.println("<empty>");
    		}
    		if (loopBody != null) {
    			loopBody.printTo(pw);
    		}
    		pw.decIndent();
    	}
   }

    /**
      * An "if ( ) { } else { }" block
      */
    public static class If extends Tree {
    	
    	public Expr condition;
    	public Tree trueBranch;
    	public Tree falseBranch;

        public If(Expr condition, Tree trueBranch, Tree falseBranch,
    			Location loc) {
            super(TAG.IF, loc);
            this.condition = condition;
    		this.trueBranch = trueBranch;
    		this.falseBranch = falseBranch;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitIf(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("if");
    		pw.incIndent();
    		condition.printTo(pw);
    		if (trueBranch != null) {
    			trueBranch.printTo(pw);
    		}
    		pw.decIndent();
    		if (falseBranch != null) {
    			pw.println("else");
    			pw.incIndent();
    			falseBranch.printTo(pw);
    			pw.decIndent();
    		}
    	}
    }
    
    public static class Switch extends Tree {
    	
    	public Expr expr;
    	public List<CaseStmt> branches;
    	public LocalScope associatedScope;

        public Switch(Expr expr, List<CaseStmt> branches, Location loc) {
            super(TAG.SWITCH, loc);
            this.expr = expr;
    		this.branches = branches;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitSwitch(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("switch ");
    		pw.incIndent();
    		expr.printTo(pw);
    		for (CaseStmt cs : branches) {
    			cs.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }
    
    public static class CaseStmt extends Tree {
    	
    	public Expr condition;
    	public List<Tree> stmts;
    	
    	public CaseStmt(Expr condition, List<Tree> stmts, Location loc) {
    		super(TAG.CASESTMT, loc);
    		this.condition = condition;
    		this.stmts = stmts;
    	}
    	
    	@Override
        public void accept(Visitor v) {
            v.visitCaseStmt(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		if (this.condition != null) {
    			pw.println("case condition:");
    			pw.incIndent();
    			condition.printTo(pw);
    			pw.decIndent();
    			pw.println("case stmt : ");
    		} else {
    			pw.println("default : ");
    		}
    		pw.incIndent();
        	for (Tree s : stmts) {
        			s.printTo(pw);
        	}
        	pw.decIndent();
    	}
    	
    }

    /**
      * A break from a loop.
      */
    public static class Break extends Tree {

        public Break(Location loc) {
            super(TAG.BREAK, loc);
        }

    	@Override
        public void accept(Visitor v) {
            v.visitBreak(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("break");
    	}
    }

    /**
      * Printf function
      */
    public static class Printf extends Tree {

    	public List<Expr> exprs;

    	public Printf(List<Expr> exprs, Location loc) {
    		super(TAG.PRINTF, loc);
    		this.exprs = exprs;
    	}

        @Override
        public void accept(Visitor v) {
            v.visitPrintf(this);
        }

        @Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("printf");
    		pw.incIndent();
    		for (Expr e : exprs) {
    			e.printTo(pw);
    		}
    		pw.decIndent();
        }
    }
    
    public static class Scanf extends Tree {

    	public List<Expr> exprs;

    	public Scanf(List<Expr> exprs, Location loc) {
    		super(TAG.SCANF, loc);
    		this.exprs = exprs;
    	}

        @Override
        public void accept(Visitor v) {
            v.visitScanf(this);
        }

        @Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("scanf");
    		pw.incIndent();
    		for (Expr e : exprs) {
    			e.printTo(pw);
    		}
    		pw.decIndent();
        }
    }
    
    public static class Alloc extends Expr {
    	
    	public Expr expr;
    	
    	public Alloc(Expr expr, Location loc) {
    		super(TAG.ALLOC, loc);
    		this.expr = expr;
    	}

        @Override
        public void accept(Visitor v) {
            v.visitAlloc(this);
        }

        @Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("malloc");
    		pw.incIndent();
    		expr.printTo(pw);
    		pw.decIndent();
        }
    }
    
    public static class SizeOf extends Expr {
    	
    	public TypeLiteral basic_type;
    	public int pointer_num;
    	
    	public SizeOf(TypeLiteral basic_type, int pointer_num, Location loc) {
    		super(TAG.SIZEOF, loc);
    		this.basic_type = basic_type;
    		this.pointer_num = pointer_num;
    	}

        @Override
        public void accept(Visitor v) {
            v.visitSizeOf(this);
        }

        @Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("sizeof ( ");
    		basic_type.printTo(pw);
    		for (int i = 0; i < pointer_num; i++) {
    			pw.print("*");
    		}
    		pw.print(" )");
    		pw.println();
        }
    }

    /**
      * A return statement.
      */
    public static class Return extends Tree {

    	public Expr expr;
    	public Type returnType;

        public Return(Expr expr, Location loc) {
            super(TAG.RETURN, loc);
            this.expr = expr;
        }

        @Override
        public void accept(Visitor v) {
            v.visitReturn(this);
        }

        @Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("return");
    		if (expr != null) {
    			pw.incIndent();
    			expr.printTo(pw);
    			pw.decIndent();
    		}
    	}
    }

    public abstract static class Expr extends Tree {

    	public Type type;
    	public Temp val;
    	
    	public Expr(TAG tag, Location loc) {
    		super(tag, loc);
    	}
    }

    public abstract static class LValue extends Expr {

    	public enum Kind {
    		LOCAL_VAR, PARAM_VAR, GLOBAL_VAR, /*POINTER_MEMBER_VAR, MEMBER_VAR, POINTER_TARGET, */ARRAY_ELEMENT
    	}
    	public Kind lvKind;
    	
    	LValue(TAG tag, Location loc) {
    		super(tag, loc);
    	}
    }

    /**
      * A unary operation.
      */
    public static class Unary extends Expr {

    	public Expr expr;

        public Unary(TAG kind, Expr expr, Location loc) {
            super(kind, loc);
    		this.expr = expr;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitUnary(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (tag) {
    		case GETADDR:
    			unaryOperatorToString(pw, "get_addr");
    			break;
    		case SUF_SA:
    			unaryOperatorToString(pw, "suffix_self_add");
    			break;
    		case SUF_SS:
    			unaryOperatorToString(pw, "suffix_self_sub");
    			break;
    		case PRE_SA:
    			unaryOperatorToString(pw, "prefix_self_add");
    			break;
    		case PRE_SS:
    			unaryOperatorToString(pw, "prefix_self_sub");
    			break;
    		case NEG:
    			unaryOperatorToString(pw, "neg");
    			break;
    		case POS:
    			unaryOperatorToString(pw, "pos");
    			break;
    		case NOT:
    			unaryOperatorToString(pw, "not");
    			break;
			}
    	}
    	
    	private void unaryOperatorToString(IndentPrintWriter pw, String op) {
    		pw.println(op);
    		pw.incIndent();
    		expr.printTo(pw);
    		pw.decIndent();
    	}
   }

    /**
      * A binary operation.
      */
    public static class Binary extends Expr {

    	public Expr left;
    	public Expr right;

        public Binary(TAG kind, Expr left, Expr right, Location loc) {
            super(kind, loc);
    		this.left = left;
    		this.right = right;
        }

    	private void binaryOperatorPrintTo(IndentPrintWriter pw, String op) {
    		pw.println(op);
    		pw.incIndent();
    		left.printTo(pw);
    		right.printTo(pw);
    		pw.decIndent();
    	}

    	public static String opStr(TAG kind) {
    		switch (kind) {
    		case AND:
    			return "&&";
    		case EQ:
    			return "==";
    		case GE:
    			return ">=";
    		case LE:
    			return "<=";
    		case NE:
    			return "!=";
    		case OR:
    			return "||";
    		case LT:
    			return "<";
    		case GT:
    			return ">";
    		case PLUS:
    			return "+";
    		case MINUS:
    			return "-";
    		case MUL:
    			return "*";
    		case DIV:
    			return "/";
    		case MOD:
    			return "%";
    		case SE:
    			return "-=";
    		case AE:
    			return "+=";
    		case ASSIGN:
    			return "=";
    		default:
    			return "" + kind;
    		}
    	}
    	
    	@Override
    	public void accept(Visitor visitor) {
    		visitor.visitBinary(this);
    	}

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (tag) {
    		case ASSIGN:
    			binaryOperatorPrintTo(pw, "assign");
    			break;
    		case AE:
    			binaryOperatorPrintTo(pw, "add_equal");
    			break;
    		case SE:
    			binaryOperatorPrintTo(pw, "sub_equal");
    			break;
    		case PLUS:
    			binaryOperatorPrintTo(pw, "add");
    			break;
    		case MINUS:
    			binaryOperatorPrintTo(pw, "sub");
    			break;
    		case MUL:
    			binaryOperatorPrintTo(pw, "mul");
    			break;
    		case DIV:
    			binaryOperatorPrintTo(pw, "div");
    			break;
    		case MOD:
    			binaryOperatorPrintTo(pw, "mod");
    			break;
    		case AND:
    			binaryOperatorPrintTo(pw, "and");
    			break;
    		case OR:
    			binaryOperatorPrintTo(pw, "or");
    			break;
    		case EQ:
    			binaryOperatorPrintTo(pw, "equ");
    			break;
    		case NE:
    			binaryOperatorPrintTo(pw, "neq");
    			break;
    		case LT:
    			binaryOperatorPrintTo(pw, "les");
    			break;
    		case LE:
    			binaryOperatorPrintTo(pw, "leq");
    			break;
    		case GT:
    			binaryOperatorPrintTo(pw, "gtr");
    			break;
    		case GE:
    			binaryOperatorPrintTo(pw, "geq");
    			break;
    		}
    	}
    }

    public static class CallExpr extends Expr {

    	public String method;
    	public List<Expr> actuals;
    	public Function symbol;

    	public CallExpr(String method, List<Expr> actuals,
    			Location loc) {
    		super(TAG.CALLEXPR, loc);
    		this.method = method;
    		this.actuals = actuals;
    	}

    	@Override
    	public void accept(Visitor visitor) {
    		visitor.visitCallExpr(this);
    	}

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("call " + method);
    		pw.incIndent();
    		for (Expr e : actuals) {
    			e.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }

    /**
      * A type cast.
      */
    public static class TypeCast extends Expr {

    	public TypeLiteral basic_type;
    	public int pointer_num;
    	public Expr expr;

        public TypeCast(TypeLiteral basic_type, int pointer_num, Expr expr, Location loc) {
            super(TAG.TYPECAST, loc);
    		this.basic_type = basic_type;
    		this.pointer_num = pointer_num;
    		this.expr = expr;
       }

    	@Override
        public void accept(Visitor v) {
            v.visitTypeCast(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("typecast");
    		pw.incIndent();
    		basic_type.printTo(pw);
    		for (int i = 0; i < pointer_num; i++) {
    			pw.print("*");
    		}
    		pw.println();
    		expr.printTo(pw);
    		pw.decIndent();
    	}
    }

    /**
      * An array selection by '[' expr ']' or by '*'
      */
    public static class Indexed extends LValue {

    	public Expr pointer;
    	public Expr index;

        public Indexed(Expr pointer, Expr index, Location loc) {
            super(TAG.INDEXED, loc);
    		this.pointer = pointer;
    		this.index = index;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitIndexed(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("indexed");
    		pw.incIndent();
    		pointer.printTo(pw);
    		if (index != null) {
    			index.printTo(pw);
    		}
    		pw.decIndent();
    	}
    }
    
    /**
     * A get content operation by operator '&'
     */
//    public static class GetAddress extends Expr {
//
//    	public Expr lvalue;
//
//        public GetAddress(Expr lvalue, Location loc) {
//            super(GETADDR, loc);
//    		this.lvalue = lvalue;
//        }
//
//    	@Override
//        public void accept(Visitor v) {
//            v.visitGetAddress(this);
//        }
//
//    	@Override
//    	public void printTo(IndentPrintWriter pw) {
//    		pw.println("getaddr");
//    		pw.incIndent();
//    		lvalue.printTo(pw);
//    		pw.decIndent();
//    	}
//    }
    
    /**
      * An identifier
      */
    public static class Ident extends LValue {

    	public String name;
    	public Variable symbol;

        public Ident(String name, Location loc) {
            super(TAG.IDENT, loc);
    		this.name = name;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitIdent(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.println("varref " + name);
    	}
    }

    /**
      * A constant value given literally.
      * @param value value representation
      */
    public static class Literal extends Expr {

    	public int typeTag;
        public Object value;

        public Literal(int typeTag, Object value, Location loc) {
            super(TAG.LITERAL, loc);
            this.typeTag = typeTag;
            this.value = value;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitLiteral(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (typeTag) {
    		case INT:
    			pw.println("intconst " + value);
    			break;
    		case BOOL:
    			pw.println("boolconst " + value);
    			break;
    		case CHAR:
    			pw.println("charconst " + MiscUtils.quote((String)value));
    			break;
    		case FLOAT:
    			pw.println("floatconst " + value);
    			break;
    		default:
    			pw.println("stringconst " + MiscUtils.quote((String)value));
    		}
    	}
    }
    
    public static class Null extends Expr {

        public Null(Location loc) {
            super(TAG.NULL, loc);
        }

    	@Override
        public void accept(Visitor v) {
            v.visitNull(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
  			pw.println("null");
    	}
    }

    public static abstract class TypeLiteral extends Tree {
    	
    	public TypeLiteral(TAG tag, Location loc){
    		super(tag, loc);
    	}
    }
    
    /**
      * Identifies a basic type.
      * @param tag the basic type id
      */
    public static class TypeIdent extends TypeLiteral {
    	
        public int typeTag;

        public TypeIdent(int typeTag, Location loc) {
            super(TAG.TYPEIDENT, loc);
            this.typeTag = typeTag;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitTypeIdent(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		switch (typeTag){
    		case INT:
    			pw.print("inttype");
    			break;
    		case FLOAT:
    			pw.print("floattype");
    			break;
    		case BOOL:
    			pw.print("booltype");
    			break;
    		case VOID:
    			pw.print("voidtype");
    			break;
    		default:
    			pw.print("chartype");
    		}
    	}
    }

    /**
      * An pointer type, A*
      */
    public static class TypePointer extends TypeLiteral {

    	public TypeLiteral targetType;

        public TypePointer(TypeLiteral targetType, Location loc) {
            super(TAG.TYPEPOINTER, loc);
    		this.targetType = targetType;
        }

    	@Override
        public void accept(Visitor v) {
            v.visitTypePointer(this);
        }

    	@Override
    	public void printTo(IndentPrintWriter pw) {
    		pw.print("pointertype ");
    		targetType.printTo(pw);
    	}
    }

    /**
      * A generic visitor class for trees.
      */
    public static abstract class Visitor {

        public Visitor() {
            super();
        }

		public void visitSizeOf(SizeOf that) {
			visitTree(that);
        }

		public void visitAlloc(Alloc that) {
			visitTree(that);
        }

		public void visitFormalDef(FormalDef that) {
			visitTree(that);
        }

		public void visitScanf(Scanf that) {
			visitTree(that);
        }

		public void visitPrintf(Printf that) {
			visitTree(that);
        }

		public void visitCaseStmt(CaseStmt that) {
			visitTree(that);
        }

		public void visitSwitch(Switch that) {
			visitTree(that);
        }

		public void visitVarComp(VarComp that) {
			visitTree(that);
        }

		public void visitInitVar(InitVar that) {
			visitTree(that);
        }

		public void visitTopLevel(TopLevel that) {
            visitTree(that);
        }
        
        public void visitControlLine(ControlLine that) {
        	visitTree(that);
        }

        public void visitMethodDeclare(MethodDeclare that) {
        	visitTree(that);
		}
        
        public void visitMethodDef(MethodDef that) {
            visitTree(that);
        }
        
        public void visitConDef(ConDef that) {
			visitTree(that);
        
        }
        public void visitVarDef(VarDef that) {
            visitTree(that);
        }

        public void visitSkip(Skip that) {
            visitTree(that);
        }

        public void visitBlock(Block that) {
            visitTree(that);
        }

        public void visitWhileLoop(WhileLoop that) {
            visitTree(that);
        }

        public void visitForLoop(ForLoop that) {
            visitTree(that);
        }

        public void visitIf(If that) {
            visitTree(that);
        }

        public void visitBreak(Break that) {
            visitTree(that);
        }

        public void visitReturn(Return that) {
            visitTree(that);
        }

        public void visitUnary(Unary that) {
            visitTree(that);
        }

        public void visitBinary(Binary that) {
            visitTree(that);
        }

        public void visitCallExpr(CallExpr that) {
            visitTree(that);
        }

        public void visitLValue(LValue that) {
            visitTree(that);
        }

        public void visitTypeCast(TypeCast that) {
            visitTree(that);
        }

        public void visitIndexed(Indexed that) {
            visitTree(that);
        }

        public void visitIdent(Ident that) {
            visitTree(that);
        }

        public void visitLiteral(Literal that) {
            visitTree(that);
        }

        public void visitNull(Null that) {
            visitTree(that);
        }

        public void visitTypeIdent(TypeIdent that) {
            visitTree(that);
        }

        public void visitTypePointer(TypePointer that) {
            visitTree(that);
        }

        public void visitTree(Tree that) {
            assert false;
        }
    }
}
