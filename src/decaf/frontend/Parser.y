/*
 * 本文件提供实现Decaf编译器所需要的BYACC脚本。
 * 在第一阶段中你需要补充完整这个文件中的语法规则。
 * 请参考"YACC--Yet Another Compiler Compiler"中关于如何编写BYACC脚本的说明。
 * 
 * Keltin Leung
 * DCST, Tsinghua University
 */
 
%{
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
%}

%Jclass Parser
%Jextends BaseParser
%Jsemantic SemValue
%Jimplements ReduceListener
%Jnorun
%Jnodebug
%Jnoconstruct

%token VOID   BOOL  INT  CHAR   FLOAT   CONST
%token NULL   WHILE   FOR   DEFINE   NEW_LINE  SWITCH
%token IF   ELSE  RETURN   BREAK   NEW   EMPTY  CASE  DEFAULT
%token PRINTF  SCANF  LITERAL  GET_CONTENT  MALLOC  SIZEOF
%token IDENTIFIER	  AND    OR   UMINUS   UPLUS
%token LESS_EQUAL   GREATER_EQUAL  EQUAL   NOT_EQUAL
%token SELF_ADD    SELF_SUB    ADD_EQUAL   SUB_EQUAL
%token PREFIX_SELF_ADD  PREFIX_SELF_SUB   SUFFIX_SELF_ADD   SUFFIX_SELF_SUB
%token '+'  '-'  '*'  '/'  '%'  '='  '>'  '<'  '.'  ':'
%token ','  ';'  '!'  '('  ')'  '['  ']'  '{'  '}'  '&'

%nonassoc REDUCE
%right ADD_EQUAL SUB_EQUAL '='
%left OR
%left AND
%right '&'
%left EQUAL NOT_EQUAL
%left LESS_EQUAL GREATER_EQUAL '<' '>' 
%left  '+' '-'
%left  '*' '/' '%'
%right '!' UMINUS UPLUS GET_CONTENT PREFIX_SELF_ADD PREFIX_SELF_SUB
%left SUFFIX_SELF_ADD SUFFIX_SELF_SUB
%nonassoc ')'
%nonassoc '[' '.' EMPTY
%nonassoc ELSE

%start Program

%%
Program            : ControlLinesList DeclaresList
              		  {
                      tree = new Tree.TopLevel($1.clist, $2.dlist, $2.loc);
                    }
              		 ;
				
ControlLinesList   : ControlLinesList ControlLine
                    {
                      $$.clist.add($2.cline);
                    }
                   | /* empty */
                    {
                      $$ = new SemValue();
                      $$.clist = new ArrayList<Tree.ControlLine>();
                    }
                   ;
                    
ControlLine        : '#' DEFINE IDENTIFIER Constant NEW_LINE
                    {
                      $$.cline = new Tree.ControlLine($3.ident, $4.expr, $3.loc);
                    }
                   ;
                    
DeclaresList       : DeclaresList Declare
                    {
                      $$.dlist.add($2.declare);
                    }
                   | /* empty */
                    {
                      $$ = new SemValue();
                      $$.dlist = new ArrayList<Tree>();
                    }
                   ;
                    
Declare            : ConstantDef
                    {
                      $$.declare = $1.cdef;
                    }
                   | VariableDef
                    {
                      $$.declare = $1.vdef; 
                    }
                   | FunctionDeclare
                    {
                      $$.declare = $1.mdcl;
                    }
                   | FunctionDef
                    {
                      $$.declare = $1.mdef;
                    }
                   ;
                   
ConstantDef        : CONST Type InitVarList ';'
                    {
                      $$.cdef = new Tree.ConDef($3.ivlt, $2.type, $2.loc);
                    }
                   ;

InitVarList        : InitVarList ',' InitVar
                    {
                      $$.ivlt.add($3.ivar);
                    }
                   | InitVar
                    {
                      $$.ivlt = new ArrayList<Tree.InitVar>();
                      $$.ivlt.add($1.ivar);
                    }
                   ;
                    
InitVar            : IDENTIFIER '=' Constant
                    {
                      $$.ivar = new Tree.InitVar($1.ident, $3.expr, $1.loc);
                    }
                   ;
                   
VariableDef        : Type VarComplementList ';'
                    {
                      $$.vdef = new Tree.VarDef($2.vclt, $1.type, $1.loc);
                    }
                   ;

VarComplementList  : VarComplementList ',' VarComplement
                    {
                      $$.vclt.add($3.vcpt);
                    }
                   | VarComplement
                    {
                      $$.vclt = new ArrayList<Tree.VarComp>();
                      $$.vclt.add($1.vcpt);
                    }
                   ;
                   
VarComplement      : PointerMarks IDENTIFIER '[' Constant ']'
                    {
                      $$.vcpt = new Tree.VarComp($2.ident, $1.marks.size(), $4.expr, null, $2.loc);
                    }
                   | PointerMarks IDENTIFIER '=' Expr
                    {
                      $$.vcpt = new Tree.VarComp($2.ident, $1.marks.size(), null, $4.expr, $2.loc);
                    }
                   | PointerMarks IDENTIFIER
                    {
                      $$.vcpt = new Tree.VarComp($2.ident, $1.marks.size(), null, null, $2.loc);
                    }
                   ;
                   
PointerMarks       : PointerMarks '*'
                    {
                      $$.marks.add("*");
                    }
                   | /* empty */
                    {
                      $$ = new SemValue();
                      $$.marks = new ArrayList<String>();
                    }
                   ;
                   
Type               : INT
                    {
                      $$.type = new Tree.TypeIdent(Tree.INT, $1.loc);
                    }
                   | FLOAT
                    {
                      $$.type = new Tree.TypeIdent(Tree.FLOAT, $1.loc);
                    }
                   | BOOL
                    {
                      $$.type = new Tree.TypeIdent(Tree.BOOL, $1.loc); 
                    }
                   | CHAR
                    {
                      $$.type = new Tree.TypeIdent(Tree.CHAR, $1.loc); 
                    }
                   | VOID
                    {
                      $$.type = new Tree.TypeIdent(Tree.VOID, $1.loc); 
                    }
                   ;
                   
FunctionDeclare    : Type PointerMarks IDENTIFIER '(' Formals ')' ';'
                    {
                      $$.mdcl = new Tree.MethodDeclare($3.ident, $1.type, $2.marks.size(), $5.flist, $3.loc);
                    }
                   ;
                   
FunctionDef        : Type PointerMarks IDENTIFIER '(' Formals ')' StmtBlock
                    {
                      $$.mdef = new Tree.MethodDef($3.ident, $1.type, $2.marks.size(), $5.flist, (Tree.Block) $7.stmt, $3.loc);
                    }
                   ;
                   
Formals            : FormalsList
                   | /* empty */
                    {
                      $$ = new SemValue();
                      $$.flist = new ArrayList<Tree.FormalDef>();
                    }
                   ;

FormalsList        : FormalsList ',' FormalDef
                    {
                      $$.flist.add($3.fdef);
                    }
                   | FormalDef
                    {
                      $$.flist = new ArrayList<Tree.FormalDef>();
                      $$.flist.add($1.fdef);
                    }
                   ;
                   
FormalDef           : Type PointerMarks IDENTIFIER
                    {
                      $$.fdef = new Tree.FormalDef($3.ident, $2.marks.size(), $1.type, $3.loc);
                    }
                   ;
                   
StmtBlock          : '{' StmtList '}'
                    {
                      $$.stmt = new Tree.Block($2.slist, $1.loc);
                    }
                   ;
                   
StmtList           : StmtList Stmt
                    {
                      $$.slist.add($2.stmt);
                    }
                   | /* empty */
                    {
                      $$ = new SemValue();
                      $$.slist = new ArrayList<Tree>();
                    }
                   ;
                   
Stmt               : ConstantDef
                    {
                      $$.stmt = $1.cdef;
                    }
                   | VariableDef
                    {
                      $$.stmt = $1.vdef;
                    }
                   | Expr ';'
                    {
                      $$.stmt = $1.expr;
                    }
                   | IfStmt
                   | WhileStmt
                   | ForStmt
                   | SwitchStmt
                   | BreakStmt ';'
                   | ReturnStmt ';'
                   | PrintStmt ';'
                   | ReadStmt ';'
                   | StmtBlock
                   | ';'
                    {
                      $$.stmt = new Tree.Skip($1.loc);
                    }
                   ;
                   
ExprOpt            : Expr
                    {
                      $$.stmt = $1.expr;
                    }
                   | /* empty */
                    {
                      $$ = new SemValue();
                    }
                   ;
                   
Call               : IDENTIFIER '(' Actuals ')'
                    {
                      $$.expr = new Tree.CallExpr($1.ident, $3.elist, $1.loc);
                    }
                   ;
                   
Alloc              : MALLOC '(' Expr ')'
                    {
                      $$.expr = new Tree.Alloc($3.expr, $1.loc);
                    }
                   ;
                   
SizeOf             : SIZEOF '(' Type PointerMarks ')'
                    {
                      $$.expr = new Tree.SizeOf($3.type, $4.marks.size(), $1.loc);
                    }
                   ;
                   
Actuals            : ActualsList
                   | /* empty */
                    {
                      $$ = new SemValue();
                      $$.elist = new ArrayList<Tree.Expr>();
                    }
                   ;
                   
ActualsList        : ActualsList ',' Expr
                    {
                      $$.elist.add($3.expr);
                    }
                   | Expr
                    {
                      $$.elist = new ArrayList<Tree.Expr>();
                      $$.elist.add($1.expr);
                    }
                   ;
                   
ForStmt            : FOR '(' ExprOpt ';' Expr ';' ExprOpt ')' Stmt
                    {
                      $$.stmt = new Tree.ForLoop($3.stmt, $5.expr, $7.stmt, $9.stmt, $1.loc);
                    }
                   ;
                   
WhileStmt          : WHILE '(' Expr ')' Stmt
                    {
                      $$.stmt = new Tree.WhileLoop($3.expr, $5.stmt, $1.loc);
                    }
                   ;
                   
IfStmt             : IF '(' Expr ')' Stmt ElseClause
                    {
                      $$.stmt = new Tree.If($3.expr, $5.stmt, $6.stmt, $1.loc);
                    }
                   ;
                   
ElseClause         : ELSE Stmt
                    {
                      $$.stmt = $2.stmt;
                    }
                   | /* empty */                %prec EMPTY
                    {
                      $$ = new SemValue();
                    }
                   ;
                   
SwitchStmt         : SWITCH '(' Expr ')' '{' CaseList '}'
                    {
                      $$.stmt = new Tree.Switch($3.expr, $6.cslist, $1.loc);
                    }
                   ;
                   
CaseList           : CaseList SingleCase
                    {
                      $$.cslist.add($2.csst);
                    }
                   | SingleCase
                    {
                      $$.cslist = new ArrayList<Tree.CaseStmt>();
                      $$.cslist.add($1.csst);
                    }
                   ;

SingleCase         : CASE Constant ':' StmtList
                    {
                      $$.csst = new Tree.CaseStmt($2.expr, $4.slist, $1.loc);
                    }
                   | DEFAULT ':' StmtList
                    {
                      $$.csst = new Tree.CaseStmt(null, $3.slist, $1.loc);
                    }
                   ;
                   
ReturnStmt         : RETURN Expr
                    {
                      $$.stmt = new Tree.Return($2.expr, $1.loc);
                    }
                   | RETURN
                    {
                      $$.stmt = new Tree.Return(null, $1.loc);
                    }
                   ;
                   
BreakStmt          : BREAK
                    {
                      $$.stmt = new Tree.Break($1.loc);
                    }
                   ;
                   
ReadStmt           : SCANF '(' LValueList ')'
                    {
                      $$.stmt = new Scanf($3.elist, $1.loc);
                    }
                   ;
                   
PrintStmt          : PRINTF '(' ActualsList ')'
                    {
                      $$.stmt = new Printf($3.elist, $1.loc);
                    }
                   ;
                   
LValueList         : LValueList ',' LValue
                    {
                      $$.elist.add($3.lvalue);
                    }
                   | LValue
                    {
                      $$.elist = new ArrayList<Tree.Expr>();
                      $$.elist.add($1.lvalue);
                    }
                   ;
                   
LValue             : IDENTIFIER
                    {
                      $$.lvalue = new Tree.Ident($1.ident, $1.loc);
                    }
                   | '(' Expr ')' '[' Expr ']'
                    {
                      $$.lvalue = new Tree.Indexed($2.expr, $5.expr, $1.loc);
                    }
                   | LValue '[' Expr ']'
                    {
                      $$.lvalue = new Tree.Indexed($1.lvalue, $3.expr, $1.loc);
                    }
                   | Call '[' Expr ']'
                    {
                      $$.lvalue = new Tree.Indexed($1.expr, $3.expr, $1.loc);
                    }
                   | '*' Expr                   %prec GET_CONTENT
                    {
                      $$.lvalue = new Tree.Indexed($2.expr, null, $1.loc);
                    }
                   | '(' LValue ')'
                    {
                      $$ = $2;
                    }
                   ;
                   
Expr               : Constant
                   | Alloc
                   | SizeOf
                   | Call                       %prec REDUCE
                   | LValue                     %prec REDUCE
                    {
                      $$.expr = $1.lvalue;
                    }
                   | SELF_ADD LValue            %prec PREFIX_SELF_ADD
                    {
                      $$.expr = new Tree.Unary(Tree.TAG.PRE_SA, $2.lvalue, $1.loc);
                    }
                   | SELF_SUB LValue            %prec PREFIX_SELF_SUB
                    {
                      $$.expr = new Tree.Unary(Tree.TAG.PRE_SS, $2.lvalue, $1.loc);
                    }
                   | LValue '=' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.ASSIGN, $1.lvalue, $3.expr, $1.loc);
                    }
                   | LValue ADD_EQUAL Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.AE, $1.lvalue, $3.expr, $1.loc);
                    }
                   | LValue SUB_EQUAL Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.SE, $1.lvalue, $3.expr, $1.loc);
                    }
                   | LValue SELF_ADD            %prec SUFFIX_SELF_ADD
                    {
                      $$.expr = new Tree.Unary(Tree.TAG.SUF_SA, $1.lvalue, $1.loc);
                    }
                   | LValue SELF_SUB            %prec SUFFIX_SELF_SUB
                    {
                      $$.expr = new Tree.Unary(Tree.TAG.SUF_SS, $1.lvalue, $1.loc);
                    }
                   | '(' Expr ')'
                    {
                      $$ = $2;
                    }
                   | Expr '+' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.PLUS, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr '-' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.MINUS, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr '*' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.MUL, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr '/' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.DIV, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr '%' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.MOD, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr '<' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.LT, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr LESS_EQUAL Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.LE, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr '>' Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.GT, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr GREATER_EQUAL Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.GE, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr EQUAL Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.EQ, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr NOT_EQUAL Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.NE, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr AND Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.AND, $1.expr, $3.expr, $2.loc);
                    }
                   | Expr OR Expr
                    {
                      $$.expr = new Tree.Binary(Tree.TAG.OR, $1.expr, $3.expr, $2.loc);
                    }
                   | '-' Expr                   %prec UMINUS
                    {
                      $$.expr = new Tree.Unary(Tree.TAG.NEG, $2.expr, $1.loc);
                    }
                   | '+' Expr                   %prec UPLUS
                    {
                      $$.expr = new Tree.Unary(Tree.TAG.POS, $2.expr, $1.loc);
                    }
                   | '!' Expr
                    {
                      $$.expr = new Tree.Unary(Tree.TAG.NOT, $2.expr, $1.loc);
                    }
                   | '(' Type PointerMarks ')' Expr
                    {
                      $$.expr = new Tree.TypeCast($2.type, $3.marks.size(), $5.expr, $2.loc);
                    }
                   ;
                   
Constant           : LITERAL
                    {
                      $$.expr = new Tree.Literal($1.typeTag, $1.literal, $1.loc);
                    }
                   | NULL
                    {
                      $$.expr = new Null($1.loc);
                    }
                   ;

%%
    
	/**
	 * 打印当前归约所用的语法规则<br>
	 * 请勿修改。
	 */
    public boolean onReduce(String rule) {
		if (rule.startsWith("$$"))
			return false;
		else
			rule = rule.replaceAll(" \\$\\$\\d+", "");

   	    if (rule.endsWith(":"))
    	    System.out.println(rule + " <empty>");
   	    else
			System.out.println(rule);
		return false;
    }
    
    public void diagnose() {
		addReduceListener(this);
		yyparse();
	}