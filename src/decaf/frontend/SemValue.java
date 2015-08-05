package decaf.frontend;

import java.util.List;

import decaf.Location;
import decaf.tree.Tree;
import decaf.tree.Tree.CaseStmt;
import decaf.tree.Tree.ConDef;
import decaf.tree.Tree.ControlLine;
import decaf.tree.Tree.Expr;
import decaf.tree.Tree.FormalDef;
import decaf.tree.Tree.InitVar;
import decaf.tree.Tree.MethodDeclare;
import decaf.tree.Tree.MethodDef;
import decaf.tree.Tree.LValue;
import decaf.tree.Tree.TopLevel;
import decaf.tree.Tree.VarComp;
import decaf.tree.Tree.VarDef;
import decaf.tree.Tree.TypeLiteral;
import decaf.utils.MiscUtils;

public class SemValue {

	public int code;

	public Location loc;

	public int typeTag;
	
	public Object literal;
	
	public String ident;

	/**
	 * field list
	 */
	public List<Tree> dlist;

	public List<ControlLine> clist;
	
	/**
	 * statement list
	 */
	public List<Tree> slist;

	public List<Expr> elist;

	public TopLevel prog;

	public ControlLine cline;
	
	public Tree declare;

	public ConDef cdef;
	
	public List<InitVar> ivlt;
	
	public InitVar ivar;
	
	public VarDef vdef;
	
	public List<String> marks;
	
	public FormalDef fdef;
	
	public List<FormalDef> flist;
	
	public VarComp vcpt;
	
	public List<VarComp> vclt;
	
	public MethodDeclare mdcl;

	public MethodDef mdef;

	public TypeLiteral type;
	
	public CaseStmt csst;
	
	public List<CaseStmt> cslist;

	public Tree stmt;

	public Expr expr;

	public LValue lvalue;

	/**
	 * 创建一个关键字的语义值
	 * 
	 * @param code
	 *            关键字的代表码
	 * @return 对应关键字的语义值
	 */
	public static SemValue createKeyword(int code) {
		SemValue v = new SemValue();
		v.code = code;
		return v;
	}

	/**
	 * 创建一个操作符的语义值
	 * 
	 * @param code
	 *            操作符的代表码
	 * @return 对应操作符的语义值
	 */
	public static SemValue createOperator(int code) {
		SemValue v = new SemValue();
		v.code = code;
		return v;
	}

	/**
	 * 创建一个常量的语义值
	 * 
	 * @param value
	 *            常量的值
	 * @return 对应的语义值
	 */
	public static SemValue createLiteral(int tag, Object value) {
		SemValue v = new SemValue();
		v.code = Parser.LITERAL;
		v.typeTag = tag;
		v.literal = value;
		return v;
	}

	/**
	 * 创建一个标识符的语义值
	 * 
	 * @param name
	 *            标识符的名字
	 * @return 对应的语义值（标识符名字存放在sval域）
	 */
	public static SemValue createIdentifier(String name) {
		SemValue v = new SemValue();
		v.code = Parser.IDENTIFIER;
		v.ident = name;
		return v;
	}

	/**
	 * 获取这个语义值的字符串表示<br>
	 * 
	 * 我们建议你在构造词法分析器之前先阅读一下这个函数。
	 */
	public String toString() {
		String msg;
		switch (code) {
		// 关键字
		case Parser.BOOL:
			msg = "keyword  : bool";
			break;
		case Parser.BREAK:
			msg = "keyword  : break";
			break;
		case Parser.CHAR:
			msg = "keyword  : break";
			break;
		case Parser.CASE:
			msg = "keyword  : case";
			break;
		case Parser.CONST:
			msg = "keyword  : const";
			break;
		case Parser.DEFINE:
			msg = "keyword  : define";
			break;
		case Parser.ELSE:
			msg = "keyword  : else";
			break;
		case Parser.FOR:
			msg = "keyword  : for";
			break;
		case Parser.FLOAT:
			msg = "keyword  : float";
			break;
		case Parser.IF:
			msg = "keyword  : if";
			break;
		case Parser.INT:
			msg = "keyword  : int";
			break;
		case Parser.NEW:
			msg = "keyword  : new";
			break;
		case Parser.NULL:
			msg = "keyword  : NULL";
			break;
		case Parser.RETURN:
			msg = "keyword  : return";
			break;
		case Parser.SCANF:
			msg = "keyword  : scanf";
			break;
		case Parser.SWITCH:
			msg = "keyword  : switch";
			break;
		case Parser.VOID:
			msg = "keyword  : void";
			break;
		case Parser.WHILE:
			msg = "keyword  : while";
			break;
		case Parser.PRINTF:
			msg = "keyword  : printf";
			break;

		// 常量
		case Parser.LITERAL:
			switch (typeTag) {
			case Tree.INT:
			case Tree.BOOL:
			case Tree.FLOAT:
				msg = "constant : " + literal;
				break;
			default:
				msg = "constant : " + MiscUtils.quote((String)literal);
			}
			break;
			
		// 标识符
		case Parser.IDENTIFIER:
			msg = "identifier: " + ident;
			break;

		// 操作符
		case Parser.ADD_EQUAL:
			msg = "operator : +=";
			break;
		case Parser.SUB_EQUAL:
			msg = "operator : -=";
			break;
		case Parser.AND:
			msg = "operator : &&";
			break;
		case Parser.EQUAL:
			msg = "operator : ==";
			break;
		case Parser.GREATER_EQUAL:
			msg = "operator : >=";
			break;
		case Parser.LESS_EQUAL:
			msg = "operator : <=";
			break;
		case Parser.NOT_EQUAL:
			msg = "operator : !=";
			break;
		case Parser.OR:
			msg = "operator : ||";
			break;
		case Parser.SELF_ADD:
			msg = "operator : ++";
			break;
		case Parser.SELF_SUB:
			msg = "operator : --";
			break;
		default:
			msg = "operator : " + (char) code;
			break;
		}
		return (String.format("%-15s%s", loc, msg));
	}
}
