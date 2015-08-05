//### This file created by BYACC 1.8(/Java extension  1.13)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//###           14 Sep 06  -- Keltin Leung-- ReduceListener support, eliminate underflow report in error recovery
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 11 "Parser.y"
package decaf.frontend;

import decaf.tree.Tree;
import decaf.tree.Tree.*;
import decaf.error.*;
import java.util.*;
//#line 25 "Parser.java"
interface ReduceListener {
  public boolean onReduce(String rule);
}




public class Parser
             extends BaseParser
             implements ReduceListener
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

ReduceListener reduceListener = null;
void yyclearin ()       {yychar = (-1);}
void yyerrok ()         {yyerrflag=0;}
void addReduceListener(ReduceListener l) {
  reduceListener = l;}


//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:SemValue
String   yytext;//user variable to return contextual strings
SemValue yyval; //used to return semantic vals from action routines
SemValue yylval;//the 'lval' (result) I got from yylex()
SemValue valstk[] = new SemValue[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new SemValue();
  yylval=new SemValue();
  valptr=-1;
}
final void val_push(SemValue val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    SemValue[] newstack = new SemValue[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final SemValue val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final SemValue val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short VOID=257;
public final static short BOOL=258;
public final static short INT=259;
public final static short CHAR=260;
public final static short FLOAT=261;
public final static short CONST=262;
public final static short NULL=263;
public final static short WHILE=264;
public final static short FOR=265;
public final static short DEFINE=266;
public final static short NEW_LINE=267;
public final static short SWITCH=268;
public final static short IF=269;
public final static short ELSE=270;
public final static short RETURN=271;
public final static short BREAK=272;
public final static short NEW=273;
public final static short EMPTY=274;
public final static short CASE=275;
public final static short DEFAULT=276;
public final static short PRINTF=277;
public final static short SCANF=278;
public final static short LITERAL=279;
public final static short GET_CONTENT=280;
public final static short MALLOC=281;
public final static short SIZEOF=282;
public final static short IDENTIFIER=283;
public final static short AND=284;
public final static short OR=285;
public final static short UMINUS=286;
public final static short UPLUS=287;
public final static short LESS_EQUAL=288;
public final static short GREATER_EQUAL=289;
public final static short EQUAL=290;
public final static short NOT_EQUAL=291;
public final static short SELF_ADD=292;
public final static short SELF_SUB=293;
public final static short ADD_EQUAL=294;
public final static short SUB_EQUAL=295;
public final static short PREFIX_SELF_ADD=296;
public final static short PREFIX_SELF_SUB=297;
public final static short SUFFIX_SELF_ADD=298;
public final static short SUFFIX_SELF_SUB=299;
public final static short REDUCE=300;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    3,    2,    2,    5,    5,    5,    5,
    6,   11,   11,   12,    7,   13,   13,   14,   14,   14,
   15,   15,   10,   10,   10,   10,   10,    8,    9,   17,
   17,   19,   19,   20,   18,   21,   21,   22,   22,   22,
   22,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   31,   31,   32,   34,   35,   33,   33,   36,   36,   25,
   24,   23,   37,   37,   26,   38,   38,   39,   39,   28,
   28,   27,   30,   29,   40,   40,   41,   41,   41,   41,
   41,   41,   16,   16,   16,   16,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   16,   16,    4,    4,
};
final static short yylen[] = {                            2,
    2,    2,    0,    5,    2,    0,    1,    1,    1,    1,
    4,    3,    1,    3,    3,    3,    1,    5,    4,    2,
    2,    0,    1,    1,    1,    1,    1,    7,    7,    1,
    0,    3,    1,    3,    3,    2,    0,    1,    1,    2,
    1,    1,    1,    1,    2,    2,    2,    2,    1,    1,
    1,    0,    4,    4,    5,    1,    0,    3,    1,    9,
    5,    6,    2,    0,    7,    2,    1,    4,    3,    2,
    1,    1,    4,    4,    3,    1,    1,    6,    4,    4,
    2,    3,    1,    1,    1,    1,    1,    2,    2,    3,
    3,    3,    2,    2,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    2,    2,
    2,    5,    1,    1,
};
final static short yydefred[] = {                         3,
    0,    0,    0,    0,    2,    0,   27,   25,   23,   26,
   24,    0,    5,    7,    8,    9,   10,   22,    0,    0,
    0,   17,    0,  114,  113,    0,    0,    0,   13,   22,
   15,    0,   21,    4,    0,    0,   11,   16,    0,    0,
    0,    0,   14,   12,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   83,    0,    0,   84,   85,
    0,   22,    0,    0,   33,    0,    0,    0,    0,    0,
    0,    0,    0,  110,  109,   81,  111,   22,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   93,   94,    0,    0,    0,    0,
    0,    0,    0,   18,    0,   22,    0,    0,    0,    0,
    0,    0,   82,    0,    0,    0,    0,    0,    0,    0,
    0,   98,   99,  100,    0,    0,    0,    0,    0,    0,
    0,   34,   28,   37,   29,   32,   54,    0,   53,    0,
    0,    0,    0,   80,   79,    0,   55,    0,  112,    0,
    0,    0,    0,    0,    0,   72,    0,    0,   50,   35,
   38,   39,   22,    0,   49,   36,   41,   42,   43,   44,
    0,    0,    0,    0,   78,    0,    0,    0,    0,    0,
    0,    0,   40,   45,   46,   47,   48,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   74,
    0,   73,   61,    0,    0,    0,    0,    0,    0,    0,
    0,   67,    0,   62,    0,    0,   37,   65,   66,   63,
    0,   37,    0,   60,    0,
};
final static short yydgoto[] = {                          1,
    2,    4,    5,   56,   13,  161,  162,   16,   17,  163,
   28,   29,   21,   22,   39,  164,   63,  165,   64,   65,
  146,  166,  167,  168,  169,  170,  171,  172,  173,  174,
  190,   58,  108,   59,   60,  109,  214,  211,  212,  194,
   61,
};
final static short yysindex[] = {                         0,
    0,  -25, -245,  159,    0, -255,    0,    0,    0,    0,
    0, -150,    0,    0,    0,    0,    0,    0, -231, -214,
   -9,    0,  -31,    0,    0, -189,   23,    2,    0,    0,
    0,  -16,    0,    0, -231, -214,    0,    0,  -26,   79,
 -150, -231,    0,    0,  -55,   50,   52,   64,  -38,  -38,
   79,   79,   79,   79,   73,    0,  920,    7,    0,    0,
   53,    0,   84,   85,    0,   40,   79, -150,   79,   79,
    7,   51,   51,    0,    0,    0,    0,    0,  592,  134,
   79,   79,   79,   79,   79,   79,   79,   79,   79,   79,
   79,   79,   79,   79,    0,    0,   79,   79,   79,   79,
   -8,  -56, -150,    0,  601,    0,  920,  106,  105,  623,
  -33,   67,    0,  965,  941,  175,  175,   -4,   -4,   34,
   34,    0,    0,    0,  175,  175,  753,  920,  920,  920,
  767,    0,    0,    0,    0,    0,    0,   41,    0,   79,
   67,   79,   79,    0,    0,    9,    0,  920,    0,  791,
  110,  130,  136,  137,   79,    0,  143,  144,    0,    0,
    0,    0,    0,  802,    0,    0,    0,    0,    0,    0,
  101,  115,  129,  140,    0,   79,   79,   79,   79,  920,
   79,  -38,    0,    0,    0,    0,    0,  856,  920,  142,
  878,  887,   58,   59,   51,   46,   79,   66,   46,    0,
  -38,    0,    0,  899, -130,  -68,   51,   79, -231,  156,
 -103,    0,   46,    0,  166,  157,    0,    0,    0,    0,
   46,    0,   46,    0,   46,
};
final static short yyrindex[] = {                         0,
    0,    1,    0,  216,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   15,    0,    0,    0,    0,    0,    0,    0,    0,
  178,    0,    0,    0,   15,    0,    0,   94,    0,    0,
    0,    0,    0,    0,    0,    0,   28,  120,    0,    0,
  535,    0,    0,  180,    0,    0,    0,    0,  182,    0,
    0,  149,  408,    0,    0,    0,    0,    0,    0,  932,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   76,    0,  183,    0,
    0,  434,    0,  955,  974,  458,  470,  811,  907,  332,
  446,    0,    0,    0,  482,  494,    0,    3,  107, 1066,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   82,    0,    0,
    0,    0,    0,    0,  167,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  168,    0,    0,  169,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,   86,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -28,   99,  197,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -72,    0,  -70,
};
final static short yygindex[] = {                         0,
    0,    0,    0,  -12,    0,  235,  242,    0,    0,   25,
    0,  220,    0,  246,   -5, 1222,    0,  177,    0,  179,
 -152, -136,    0,    0,    0,    0,    0,    0,    0,    0,
   75,  -23,    0,    0,    0,  108,    0,    0,   83,    0,
  -30,
};
final static int YYTABLESIZE=1430;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
    6,   70,  133,   53,   64,   40,   26,  142,   33,    3,
   33,   64,   23,   64,   64,   33,   64,   51,   72,   73,
    6,  218,   43,   41,   80,   71,   71,   19,   18,   66,
   64,   24,   91,   33,   30,   42,   20,   89,   87,   80,
   88,   54,   90,   91,   40,   36,   91,   25,   55,   31,
   53,   51,   69,   52,   68,   93,  101,   92,   20,  203,
   37,   91,  206,   91,  223,   62,  134,  159,   27,  225,
   91,   19,  111,   20,   42,   89,  220,   34,   54,   78,
   90,  147,   33,   35,  224,   55,   19,   53,   51,   67,
   52,   68,  106,   91,   64,   91,   64,   94,  200,  202,
  138,  140,  201,   69,  159,   54,    7,    8,    9,   10,
   11,   54,   55,   99,   53,   51,   59,   52,   55,   59,
   53,   51,   58,   52,  102,   58,   76,   62,  103,   76,
   77,  134,  104,  160,   77,   77,   77,   77,   77,   75,
   77,  100,   75,  100,  209,  210,  139,   92,  140,  176,
   92,  195,   77,   77,   77,   77,   86,  143,   71,  184,
   86,   86,   86,   86,   86,   92,   86,   92,  134,  177,
  207,  209,  210,  185,  113,  178,  179,   71,   86,   86,
   86,   86,  181,  182,   77,   88,   77,  186,  205,   88,
   88,   88,   88,   88,   99,   88,  216,   92,  187,   92,
  197,  213,   69,   69,   68,   68,  221,   88,   88,   88,
   88,   91,   86,  217,  222,    1,   89,   87,   31,   88,
   30,   90,   57,   56,  100,   71,   52,   70,   64,   64,
   64,   64,   64,   64,   64,   64,   64,   52,   14,   64,
   64,   88,   64,   64,   48,   15,   64,   64,   64,   64,
   64,   32,   64,   64,   64,   44,   45,    6,    6,    6,
    6,    6,    6,   64,   64,    7,    8,    9,   10,   11,
   12,   24,  151,  152,  132,   38,  153,  154,  135,  155,
  156,  136,  215,   83,   84,  157,  158,   25,  193,   46,
   47,   48,    0,  219,   91,   91,   91,   91,    0,    0,
   49,   50,    7,    8,    9,   10,   11,   12,   24,  151,
  152,    0,    0,  153,  154,    0,  155,  156,    0,    0,
    0,    0,  157,  158,   25,    0,   46,   47,   48,    7,
    8,    9,   10,   11,    0,   24,    0,   49,   50,    0,
    0,   24,    0,    0,   95,   96,   97,   98,    0,    0,
    0,   25,    0,   46,   47,   48,    0,   25,    0,   46,
   47,   48,    0,    0,   49,   50,    0,    0,    0,    0,
   49,   50,   96,    0,   96,   96,   96,   77,   77,    0,
    0,   77,   77,   77,   77,   77,   77,   77,   77,    0,
   96,   96,   96,   96,    0,    0,    0,    0,   92,   92,
   92,   92,    0,   86,   86,    0,    0,   86,   86,   86,
   86,   86,   86,   86,   86,    7,    8,    9,   10,   11,
   12,    0,   96,    0,   96,   95,   96,   97,   98,    0,
    0,    0,   88,   88,    0,    0,   88,   88,   88,   88,
   88,   88,   88,   88,   89,    0,    0,    0,   89,   89,
   89,   89,   89,    0,   89,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   89,   89,   89,   89,
   95,    0,    0,    0,   95,   95,   95,   95,   95,    0,
   95,    0,    0,    0,    0,    0,   97,    0,   97,   97,
   97,    0,   95,   95,   95,   95,    0,    0,  102,    0,
   89,  102,    0,    0,   97,   97,   97,   97,    0,    0,
  104,    0,    0,  104,    0,    0,  102,  102,  102,  102,
    0,    0,  103,    0,    0,  103,   95,    0,  104,  104,
  104,  104,    0,    0,  101,    0,   97,  101,   97,    0,
  103,  103,  103,  103,    0,    0,    0,    0,  102,    0,
  102,    0,  101,  101,  101,  101,    0,    0,    0,    0,
  104,    0,  104,    0,    0,    0,    0,    0,    0,    0,
    0,   87,  103,    0,  103,   87,   87,   87,   87,   87,
    0,   87,    0,    0,  101,    0,  101,    0,    0,    0,
    0,    0,    0,   87,   87,    0,   87,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   96,   96,    0,    0,   96,
   96,   96,   96,   96,   96,   96,   96,   87,   91,    0,
    0,    0,  112,   89,   87,    0,   88,   91,   90,    0,
    0,  137,   89,   87,    0,   88,    0,   90,    0,    0,
    0,   93,    0,   92,    0,    0,    0,    0,    0,   91,
   93,    0,   92,  141,   89,   87,    0,   88,    0,   90,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   93,    0,   92,    0,    0,    0,    0,    0,
    0,   89,   89,    0,    0,   89,   89,   89,   89,   89,
   89,   89,   89,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   95,   95,    0,
    0,   95,   95,   95,   95,   95,   95,   95,   95,   97,
   97,    0,    0,   97,   97,   97,   97,   97,   97,   97,
   97,  102,  102,    0,    0,  102,  102,  102,  102,  102,
  102,  102,  102,  104,  104,    0,    0,  104,  104,  104,
  104,  104,  104,  104,  104,  103,  103,    0,    0,  103,
  103,  103,  103,  103,  103,  103,  103,  101,  101,    0,
    0,  101,  101,  101,  101,  101,  101,  101,  101,   91,
    0,    0,    0,    0,   89,   87,    0,   88,    0,   90,
    0,    0,    0,   91,    0,    0,    0,    0,   89,   87,
    0,   88,   93,   90,   92,    0,    0,    0,   87,   87,
    0,    0,   87,   87,   87,   87,   93,   91,   92,    0,
    0,    0,   89,   87,    0,   88,    0,   90,   91,    0,
    0,    0,    0,   89,   87,  144,   88,    0,   90,    0,
   93,  105,   92,    0,  105,    0,    0,    0,    0,  145,
  183,   93,    0,   92,    0,    0,    0,    0,    0,  105,
    0,  105,    0,    0,    0,   81,   82,    0,    0,   83,
   84,   85,   86,  175,   81,   82,    0,    0,   83,   84,
   85,   86,   91,    0,    0,    0,  196,   89,   87,    0,
   88,  105,   90,  105,    0,    0,   81,   82,    0,    0,
   83,   84,   85,   86,   91,   93,    0,   92,  198,   89,
   87,    0,   88,   91,   90,    0,    0,  199,   89,   87,
    0,   88,    0,   90,    0,   91,    0,   93,    0,   92,
   89,   87,    0,   88,    0,   90,   93,  106,   92,    0,
  106,    0,    0,    0,    0,    0,   91,  208,   93,    0,
   92,   89,   87,    0,   88,  106,   90,  106,   87,    0,
    0,    0,    0,   87,   87,    0,   87,   91,   87,   93,
    0,   92,   89,   87,    0,   88,    0,   90,    0,    0,
    0,   87,    0,   87,    0,  107,    0,  106,  107,  106,
   93,   91,   92,    0,    0,    0,   89,   87,    0,   88,
    0,   90,    0,  107,  108,  107,    0,  108,    0,    0,
    0,    0,    0,    0,   93,    0,   92,    0,    0,    0,
    0,    0,  108,    0,  108,    0,   81,   82,    0,    0,
   83,   84,   85,   86,    0,  107,    0,  107,    0,    0,
   81,   82,    0,    0,   83,   84,   85,   86,    0,    0,
    0,    0,    0,    0,  108,    0,  108,    0,    0,    0,
    0,    0,    0,    0,   81,   82,    0,    0,   83,   84,
   85,   86,    0,    0,    0,   81,   82,    0,    0,   83,
   84,   85,   86,    0,  105,  105,    0,    0,    0,    0,
  105,  105,  105,  105,  105,  105,   90,    0,    0,   90,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   90,    0,   90,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   81,
   82,    0,    0,   83,   84,   85,   86,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   90,    0,   90,    0,
    0,   81,   82,    0,    0,   83,   84,   85,   86,    0,
   81,   82,    0,    0,   83,   84,   85,   86,    0,    0,
    0,    0,   81,   82,    0,    0,   83,   84,   85,   86,
  106,  106,    0,    0,    0,    0,  106,  106,  106,  106,
  106,  106,    0,   81,   82,    0,    0,   83,   84,   85,
   86,    0,    0,    0,    0,   87,   87,    0,    0,   87,
   87,   87,   87,    0,   81,    0,    0,    0,   83,   84,
   85,   86,    0,    0,    0,    0,    0,    0,  107,  107,
    0,    0,    0,    0,    0,    0,  107,  107,  107,  107,
    0,    0,   83,   84,   85,   86,    0,    0,  108,    0,
    0,   57,    0,    0,    0,  108,  108,  108,  108,    0,
    0,    0,   74,   75,   76,   77,   79,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  105,    0,
  107,  110,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  114,  115,  116,  117,  118,  119,  120,  121,
  122,  123,  124,  125,  126,  127,    0,    0,  128,  129,
  130,  131,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   90,   90,   90,
   90,  148,    0,  149,  150,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  180,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  188,  189,  191,
  192,    0,  107,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  204,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  189,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   40,   59,   42,   33,   61,   19,   41,   42,   35,
   42,   40,   18,   42,   43,   42,   45,   59,   49,   50,
  266,  125,   35,   40,   55,   49,   50,  283,    4,   42,
   59,  263,   37,   42,   44,   91,   12,   42,   43,   70,
   45,   33,   47,   41,   61,   44,   44,  279,   40,   59,
   42,   43,  125,   45,  125,   60,   62,   62,   44,  196,
   59,   59,  199,   61,  217,   41,  123,   59,  283,  222,
   37,   44,   78,   59,   91,   42,  213,  267,   33,   55,
   47,   41,   42,   61,  221,   40,   59,   42,   43,   40,
   45,   40,   68,   91,  123,   93,  125,   91,   41,   41,
  106,   44,   44,   40,   59,   33,  257,  258,  259,  260,
  261,   33,   40,   61,   42,   43,   41,   45,   40,   44,
   42,   43,   41,   45,   41,   44,   41,  103,   44,   44,
   37,  123,   93,  125,   41,   42,   43,   44,   45,   41,
   47,   91,   44,   91,  275,  276,   41,   41,   44,   40,
   44,  182,   59,   60,   61,   62,   37,   91,  182,   59,
   41,   42,   43,   44,   45,   59,   47,   61,  123,   40,
  201,  275,  276,   59,   41,   40,   40,  201,   59,   60,
   61,   62,   40,   40,   91,   37,   93,   59,  123,   41,
   42,   43,   44,   45,   61,   47,  209,   91,   59,   93,
   59,  270,  275,  276,  275,  276,   41,   59,   60,   61,
   62,   37,   93,   58,   58,    0,   42,   43,   41,   45,
   41,   47,   41,   41,   91,   59,   59,   59,  257,  258,
  259,  260,  261,  262,  263,  264,  265,   41,    4,  268,
  269,   93,  271,  272,  283,    4,  275,  276,  277,  278,
  279,  283,  281,  282,  283,   36,  283,  257,  258,  259,
  260,  261,  262,  292,  293,  257,  258,  259,  260,  261,
  262,  263,  264,  265,  283,   30,  268,  269,  102,  271,
  272,  103,  208,  288,  289,  277,  278,  279,  181,  281,
  282,  283,   -1,  211,  292,  293,  294,  295,   -1,   -1,
  292,  293,  257,  258,  259,  260,  261,  262,  263,  264,
  265,   -1,   -1,  268,  269,   -1,  271,  272,   -1,   -1,
   -1,   -1,  277,  278,  279,   -1,  281,  282,  283,  257,
  258,  259,  260,  261,   -1,  263,   -1,  292,  293,   -1,
   -1,  263,   -1,   -1,  292,  293,  294,  295,   -1,   -1,
   -1,  279,   -1,  281,  282,  283,   -1,  279,   -1,  281,
  282,  283,   -1,   -1,  292,  293,   -1,   -1,   -1,   -1,
  292,  293,   41,   -1,   43,   44,   45,  284,  285,   -1,
   -1,  288,  289,  290,  291,  292,  293,  294,  295,   -1,
   59,   60,   61,   62,   -1,   -1,   -1,   -1,  292,  293,
  294,  295,   -1,  284,  285,   -1,   -1,  288,  289,  290,
  291,  292,  293,  294,  295,  257,  258,  259,  260,  261,
  262,   -1,   91,   -1,   93,  292,  293,  294,  295,   -1,
   -1,   -1,  284,  285,   -1,   -1,  288,  289,  290,  291,
  292,  293,  294,  295,   37,   -1,   -1,   -1,   41,   42,
   43,   44,   45,   -1,   47,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   59,   60,   61,   62,
   37,   -1,   -1,   -1,   41,   42,   43,   44,   45,   -1,
   47,   -1,   -1,   -1,   -1,   -1,   41,   -1,   43,   44,
   45,   -1,   59,   60,   61,   62,   -1,   -1,   41,   -1,
   93,   44,   -1,   -1,   59,   60,   61,   62,   -1,   -1,
   41,   -1,   -1,   44,   -1,   -1,   59,   60,   61,   62,
   -1,   -1,   41,   -1,   -1,   44,   93,   -1,   59,   60,
   61,   62,   -1,   -1,   41,   -1,   91,   44,   93,   -1,
   59,   60,   61,   62,   -1,   -1,   -1,   -1,   91,   -1,
   93,   -1,   59,   60,   61,   62,   -1,   -1,   -1,   -1,
   91,   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   37,   91,   -1,   93,   41,   42,   43,   44,   45,
   -1,   47,   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,
   -1,   -1,   -1,   59,   60,   -1,   62,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  284,  285,   -1,   -1,  288,
  289,  290,  291,  292,  293,  294,  295,   93,   37,   -1,
   -1,   -1,   41,   42,   43,   -1,   45,   37,   47,   -1,
   -1,   41,   42,   43,   -1,   45,   -1,   47,   -1,   -1,
   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   37,
   60,   -1,   62,   41,   42,   43,   -1,   45,   -1,   47,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,
   -1,  284,  285,   -1,   -1,  288,  289,  290,  291,  292,
  293,  294,  295,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  284,  285,   -1,
   -1,  288,  289,  290,  291,  292,  293,  294,  295,  284,
  285,   -1,   -1,  288,  289,  290,  291,  292,  293,  294,
  295,  284,  285,   -1,   -1,  288,  289,  290,  291,  292,
  293,  294,  295,  284,  285,   -1,   -1,  288,  289,  290,
  291,  292,  293,  294,  295,  284,  285,   -1,   -1,  288,
  289,  290,  291,  292,  293,  294,  295,  284,  285,   -1,
   -1,  288,  289,  290,  291,  292,  293,  294,  295,   37,
   -1,   -1,   -1,   -1,   42,   43,   -1,   45,   -1,   47,
   -1,   -1,   -1,   37,   -1,   -1,   -1,   -1,   42,   43,
   -1,   45,   60,   47,   62,   -1,   -1,   -1,  284,  285,
   -1,   -1,  288,  289,  290,  291,   60,   37,   62,   -1,
   -1,   -1,   42,   43,   -1,   45,   -1,   47,   37,   -1,
   -1,   -1,   -1,   42,   43,   93,   45,   -1,   47,   -1,
   60,   41,   62,   -1,   44,   -1,   -1,   -1,   -1,   93,
   59,   60,   -1,   62,   -1,   -1,   -1,   -1,   -1,   59,
   -1,   61,   -1,   -1,   -1,  284,  285,   -1,   -1,  288,
  289,  290,  291,   93,  284,  285,   -1,   -1,  288,  289,
  290,  291,   37,   -1,   -1,   -1,   41,   42,   43,   -1,
   45,   91,   47,   93,   -1,   -1,  284,  285,   -1,   -1,
  288,  289,  290,  291,   37,   60,   -1,   62,   41,   42,
   43,   -1,   45,   37,   47,   -1,   -1,   41,   42,   43,
   -1,   45,   -1,   47,   -1,   37,   -1,   60,   -1,   62,
   42,   43,   -1,   45,   -1,   47,   60,   41,   62,   -1,
   44,   -1,   -1,   -1,   -1,   -1,   37,   59,   60,   -1,
   62,   42,   43,   -1,   45,   59,   47,   61,   37,   -1,
   -1,   -1,   -1,   42,   43,   -1,   45,   37,   47,   60,
   -1,   62,   42,   43,   -1,   45,   -1,   47,   -1,   -1,
   -1,   60,   -1,   62,   -1,   41,   -1,   91,   44,   93,
   60,   37,   62,   -1,   -1,   -1,   42,   43,   -1,   45,
   -1,   47,   -1,   59,   41,   61,   -1,   44,   -1,   -1,
   -1,   -1,   -1,   -1,   60,   -1,   62,   -1,   -1,   -1,
   -1,   -1,   59,   -1,   61,   -1,  284,  285,   -1,   -1,
  288,  289,  290,  291,   -1,   91,   -1,   93,   -1,   -1,
  284,  285,   -1,   -1,  288,  289,  290,  291,   -1,   -1,
   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  284,  285,   -1,   -1,  288,  289,
  290,  291,   -1,   -1,   -1,  284,  285,   -1,   -1,  288,
  289,  290,  291,   -1,  284,  285,   -1,   -1,   -1,   -1,
  290,  291,  292,  293,  294,  295,   41,   -1,   -1,   44,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   59,   -1,   61,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  284,
  285,   -1,   -1,  288,  289,  290,  291,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   91,   -1,   93,   -1,
   -1,  284,  285,   -1,   -1,  288,  289,  290,  291,   -1,
  284,  285,   -1,   -1,  288,  289,  290,  291,   -1,   -1,
   -1,   -1,  284,  285,   -1,   -1,  288,  289,  290,  291,
  284,  285,   -1,   -1,   -1,   -1,  290,  291,  292,  293,
  294,  295,   -1,  284,  285,   -1,   -1,  288,  289,  290,
  291,   -1,   -1,   -1,   -1,  284,  285,   -1,   -1,  288,
  289,  290,  291,   -1,  284,   -1,   -1,   -1,  288,  289,
  290,  291,   -1,   -1,   -1,   -1,   -1,   -1,  284,  285,
   -1,   -1,   -1,   -1,   -1,   -1,  292,  293,  294,  295,
   -1,   -1,  288,  289,  290,  291,   -1,   -1,  285,   -1,
   -1,   40,   -1,   -1,   -1,  292,  293,  294,  295,   -1,
   -1,   -1,   51,   52,   53,   54,   55,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   67,   -1,
   69,   70,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   81,   82,   83,   84,   85,   86,   87,   88,
   89,   90,   91,   92,   93,   94,   -1,   -1,   97,   98,
   99,  100,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  292,  293,  294,
  295,  140,   -1,  142,  143,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  155,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  176,  177,  178,
  179,   -1,  181,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  197,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  208,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=300;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,"'#'",null,"'%'","'&'",null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"VOID","BOOL","INT","CHAR","FLOAT",
"CONST","NULL","WHILE","FOR","DEFINE","NEW_LINE","SWITCH","IF","ELSE","RETURN",
"BREAK","NEW","EMPTY","CASE","DEFAULT","PRINTF","SCANF","LITERAL","GET_CONTENT",
"MALLOC","SIZEOF","IDENTIFIER","AND","OR","UMINUS","UPLUS","LESS_EQUAL",
"GREATER_EQUAL","EQUAL","NOT_EQUAL","SELF_ADD","SELF_SUB","ADD_EQUAL",
"SUB_EQUAL","PREFIX_SELF_ADD","PREFIX_SELF_SUB","SUFFIX_SELF_ADD",
"SUFFIX_SELF_SUB","REDUCE",
};
final static String yyrule[] = {
"$accept : Program",
"Program : ControlLinesList DeclaresList",
"ControlLinesList : ControlLinesList ControlLine",
"ControlLinesList :",
"ControlLine : '#' DEFINE IDENTIFIER Constant NEW_LINE",
"DeclaresList : DeclaresList Declare",
"DeclaresList :",
"Declare : ConstantDef",
"Declare : VariableDef",
"Declare : FunctionDeclare",
"Declare : FunctionDef",
"ConstantDef : CONST Type InitVarList ';'",
"InitVarList : InitVarList ',' InitVar",
"InitVarList : InitVar",
"InitVar : IDENTIFIER '=' Constant",
"VariableDef : Type VarComplementList ';'",
"VarComplementList : VarComplementList ',' VarComplement",
"VarComplementList : VarComplement",
"VarComplement : PointerMarks IDENTIFIER '[' Constant ']'",
"VarComplement : PointerMarks IDENTIFIER '=' Expr",
"VarComplement : PointerMarks IDENTIFIER",
"PointerMarks : PointerMarks '*'",
"PointerMarks :",
"Type : INT",
"Type : FLOAT",
"Type : BOOL",
"Type : CHAR",
"Type : VOID",
"FunctionDeclare : Type PointerMarks IDENTIFIER '(' Formals ')' ';'",
"FunctionDef : Type PointerMarks IDENTIFIER '(' Formals ')' StmtBlock",
"Formals : FormalsList",
"Formals :",
"FormalsList : FormalsList ',' FormalDef",
"FormalsList : FormalDef",
"FormalDef : Type PointerMarks IDENTIFIER",
"StmtBlock : '{' StmtList '}'",
"StmtList : StmtList Stmt",
"StmtList :",
"Stmt : ConstantDef",
"Stmt : VariableDef",
"Stmt : Expr ';'",
"Stmt : IfStmt",
"Stmt : WhileStmt",
"Stmt : ForStmt",
"Stmt : SwitchStmt",
"Stmt : BreakStmt ';'",
"Stmt : ReturnStmt ';'",
"Stmt : PrintStmt ';'",
"Stmt : ReadStmt ';'",
"Stmt : StmtBlock",
"Stmt : ';'",
"ExprOpt : Expr",
"ExprOpt :",
"Call : IDENTIFIER '(' Actuals ')'",
"Alloc : MALLOC '(' Expr ')'",
"SizeOf : SIZEOF '(' Type PointerMarks ')'",
"Actuals : ActualsList",
"Actuals :",
"ActualsList : ActualsList ',' Expr",
"ActualsList : Expr",
"ForStmt : FOR '(' ExprOpt ';' Expr ';' ExprOpt ')' Stmt",
"WhileStmt : WHILE '(' Expr ')' Stmt",
"IfStmt : IF '(' Expr ')' Stmt ElseClause",
"ElseClause : ELSE Stmt",
"ElseClause :",
"SwitchStmt : SWITCH '(' Expr ')' '{' CaseList '}'",
"CaseList : CaseList SingleCase",
"CaseList : SingleCase",
"SingleCase : CASE Constant ':' StmtList",
"SingleCase : DEFAULT ':' StmtList",
"ReturnStmt : RETURN Expr",
"ReturnStmt : RETURN",
"BreakStmt : BREAK",
"ReadStmt : SCANF '(' LValueList ')'",
"PrintStmt : PRINTF '(' ActualsList ')'",
"LValueList : LValueList ',' LValue",
"LValueList : LValue",
"LValue : IDENTIFIER",
"LValue : '(' Expr ')' '[' Expr ']'",
"LValue : LValue '[' Expr ']'",
"LValue : Call '[' Expr ']'",
"LValue : '*' Expr",
"LValue : '(' LValue ')'",
"Expr : Constant",
"Expr : Alloc",
"Expr : SizeOf",
"Expr : Call",
"Expr : LValue",
"Expr : SELF_ADD LValue",
"Expr : SELF_SUB LValue",
"Expr : LValue '=' Expr",
"Expr : LValue ADD_EQUAL Expr",
"Expr : LValue SUB_EQUAL Expr",
"Expr : LValue SELF_ADD",
"Expr : LValue SELF_SUB",
"Expr : '(' Expr ')'",
"Expr : Expr '+' Expr",
"Expr : Expr '-' Expr",
"Expr : Expr '*' Expr",
"Expr : Expr '/' Expr",
"Expr : Expr '%' Expr",
"Expr : Expr '<' Expr",
"Expr : Expr LESS_EQUAL Expr",
"Expr : Expr '>' Expr",
"Expr : Expr GREATER_EQUAL Expr",
"Expr : Expr EQUAL Expr",
"Expr : Expr NOT_EQUAL Expr",
"Expr : Expr AND Expr",
"Expr : Expr OR Expr",
"Expr : '-' Expr",
"Expr : '+' Expr",
"Expr : '!' Expr",
"Expr : '(' Type PointerMarks ')' Expr",
"Constant : LITERAL",
"Constant : NULL",
};

//#line 564 "Parser.y"
    
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
//#line 733 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    //if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      //if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        //if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        //if (yychar < 0)    //it it didn't work/error
        //  {
        //  yychar = 0;      //change it to default string (no -1!)
          //if (yydebug)
          //  yylexdebug(yystate,yychar);
        //  }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        //if (yydebug)
          //debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      //if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0 || valptr<0)   //check for under & overflow here
            {
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            //if (yydebug)
              //debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            //if (yydebug)
              //debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0 || valptr<0)   //check for under & overflow here
              {
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        //if (yydebug)
          //{
          //yys = null;
          //if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          //if (yys == null) yys = "illegal-symbol";
          //debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          //}
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    //if (yydebug)
      //debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    if (reduceListener == null || reduceListener.onReduce(yyrule[yyn])) // if intercepted!
      switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 57 "Parser.y"
{
                      tree = new Tree.TopLevel(val_peek(1).clist, val_peek(0).dlist, val_peek(0).loc);
                    }
break;
case 2:
//#line 63 "Parser.y"
{
                      yyval.clist.add(val_peek(0).cline);
                    }
break;
case 3:
//#line 67 "Parser.y"
{
                      yyval = new SemValue();
                      yyval.clist = new ArrayList<Tree.ControlLine>();
                    }
break;
case 4:
//#line 74 "Parser.y"
{
                      yyval.cline = new Tree.ControlLine(val_peek(2).ident, val_peek(1).expr, val_peek(2).loc);
                    }
break;
case 5:
//#line 80 "Parser.y"
{
                      yyval.dlist.add(val_peek(0).declare);
                    }
break;
case 6:
//#line 84 "Parser.y"
{
                      yyval = new SemValue();
                      yyval.dlist = new ArrayList<Tree>();
                    }
break;
case 7:
//#line 91 "Parser.y"
{
                      yyval.declare = val_peek(0).cdef;
                    }
break;
case 8:
//#line 95 "Parser.y"
{
                      yyval.declare = val_peek(0).vdef; 
                    }
break;
case 9:
//#line 99 "Parser.y"
{
                      yyval.declare = val_peek(0).mdcl;
                    }
break;
case 10:
//#line 103 "Parser.y"
{
                      yyval.declare = val_peek(0).mdef;
                    }
break;
case 11:
//#line 109 "Parser.y"
{
                      yyval.cdef = new Tree.ConDef(val_peek(1).ivlt, val_peek(2).type, val_peek(2).loc);
                    }
break;
case 12:
//#line 115 "Parser.y"
{
                      yyval.ivlt.add(val_peek(0).ivar);
                    }
break;
case 13:
//#line 119 "Parser.y"
{
                      yyval.ivlt = new ArrayList<Tree.InitVar>();
                      yyval.ivlt.add(val_peek(0).ivar);
                    }
break;
case 14:
//#line 126 "Parser.y"
{
                      yyval.ivar = new Tree.InitVar(val_peek(2).ident, val_peek(0).expr, val_peek(2).loc);
                    }
break;
case 15:
//#line 132 "Parser.y"
{
                      yyval.vdef = new Tree.VarDef(val_peek(1).vclt, val_peek(2).type, val_peek(2).loc);
                    }
break;
case 16:
//#line 138 "Parser.y"
{
                      yyval.vclt.add(val_peek(0).vcpt);
                    }
break;
case 17:
//#line 142 "Parser.y"
{
                      yyval.vclt = new ArrayList<Tree.VarComp>();
                      yyval.vclt.add(val_peek(0).vcpt);
                    }
break;
case 18:
//#line 149 "Parser.y"
{
                      yyval.vcpt = new Tree.VarComp(val_peek(3).ident, val_peek(4).marks.size(), val_peek(1).expr, null, val_peek(3).loc);
                    }
break;
case 19:
//#line 153 "Parser.y"
{
                      yyval.vcpt = new Tree.VarComp(val_peek(2).ident, val_peek(3).marks.size(), null, val_peek(0).expr, val_peek(2).loc);
                    }
break;
case 20:
//#line 157 "Parser.y"
{
                      yyval.vcpt = new Tree.VarComp(val_peek(0).ident, val_peek(1).marks.size(), null, null, val_peek(0).loc);
                    }
break;
case 21:
//#line 163 "Parser.y"
{
                      yyval.marks.add("*");
                    }
break;
case 22:
//#line 167 "Parser.y"
{
                      yyval = new SemValue();
                      yyval.marks = new ArrayList<String>();
                    }
break;
case 23:
//#line 174 "Parser.y"
{
                      yyval.type = new Tree.TypeIdent(Tree.INT, val_peek(0).loc);
                    }
break;
case 24:
//#line 178 "Parser.y"
{
                      yyval.type = new Tree.TypeIdent(Tree.FLOAT, val_peek(0).loc);
                    }
break;
case 25:
//#line 182 "Parser.y"
{
                      yyval.type = new Tree.TypeIdent(Tree.BOOL, val_peek(0).loc); 
                    }
break;
case 26:
//#line 186 "Parser.y"
{
                      yyval.type = new Tree.TypeIdent(Tree.CHAR, val_peek(0).loc); 
                    }
break;
case 27:
//#line 190 "Parser.y"
{
                      yyval.type = new Tree.TypeIdent(Tree.VOID, val_peek(0).loc); 
                    }
break;
case 28:
//#line 196 "Parser.y"
{
                      yyval.mdcl = new Tree.MethodDeclare(val_peek(4).ident, val_peek(6).type, val_peek(5).marks.size(), val_peek(2).flist, val_peek(4).loc);
                    }
break;
case 29:
//#line 202 "Parser.y"
{
                      yyval.mdef = new Tree.MethodDef(val_peek(4).ident, val_peek(6).type, val_peek(5).marks.size(), val_peek(2).flist, (Tree.Block) val_peek(0).stmt, val_peek(4).loc);
                    }
break;
case 31:
//#line 209 "Parser.y"
{
                      yyval = new SemValue();
                      yyval.flist = new ArrayList<Tree.FormalDef>();
                    }
break;
case 32:
//#line 216 "Parser.y"
{
                      yyval.flist.add(val_peek(0).fdef);
                    }
break;
case 33:
//#line 220 "Parser.y"
{
                      yyval.flist = new ArrayList<Tree.FormalDef>();
                      yyval.flist.add(val_peek(0).fdef);
                    }
break;
case 34:
//#line 227 "Parser.y"
{
                      yyval.fdef = new Tree.FormalDef(val_peek(0).ident, val_peek(1).marks.size(), val_peek(2).type, val_peek(0).loc);
                    }
break;
case 35:
//#line 233 "Parser.y"
{
                      yyval.stmt = new Tree.Block(val_peek(1).slist, val_peek(2).loc);
                    }
break;
case 36:
//#line 239 "Parser.y"
{
                      yyval.slist.add(val_peek(0).stmt);
                    }
break;
case 37:
//#line 243 "Parser.y"
{
                      yyval = new SemValue();
                      yyval.slist = new ArrayList<Tree>();
                    }
break;
case 38:
//#line 250 "Parser.y"
{
                      yyval.stmt = val_peek(0).cdef;
                    }
break;
case 39:
//#line 254 "Parser.y"
{
                      yyval.stmt = val_peek(0).vdef;
                    }
break;
case 40:
//#line 258 "Parser.y"
{
                      yyval.stmt = val_peek(1).expr;
                    }
break;
case 50:
//#line 271 "Parser.y"
{
                      yyval.stmt = new Tree.Skip(val_peek(0).loc);
                    }
break;
case 51:
//#line 277 "Parser.y"
{
                      yyval.stmt = val_peek(0).expr;
                    }
break;
case 52:
//#line 281 "Parser.y"
{
                      yyval = new SemValue();
                    }
break;
case 53:
//#line 287 "Parser.y"
{
                      yyval.expr = new Tree.CallExpr(val_peek(3).ident, val_peek(1).elist, val_peek(3).loc);
                    }
break;
case 54:
//#line 293 "Parser.y"
{
                      yyval.expr = new Tree.Alloc(val_peek(1).expr, val_peek(3).loc);
                    }
break;
case 55:
//#line 299 "Parser.y"
{
                      yyval.expr = new Tree.SizeOf(val_peek(2).type, val_peek(1).marks.size(), val_peek(4).loc);
                    }
break;
case 57:
//#line 306 "Parser.y"
{
                      yyval = new SemValue();
                      yyval.elist = new ArrayList<Tree.Expr>();
                    }
break;
case 58:
//#line 313 "Parser.y"
{
                      yyval.elist.add(val_peek(0).expr);
                    }
break;
case 59:
//#line 317 "Parser.y"
{
                      yyval.elist = new ArrayList<Tree.Expr>();
                      yyval.elist.add(val_peek(0).expr);
                    }
break;
case 60:
//#line 324 "Parser.y"
{
                      yyval.stmt = new Tree.ForLoop(val_peek(6).stmt, val_peek(4).expr, val_peek(2).stmt, val_peek(0).stmt, val_peek(8).loc);
                    }
break;
case 61:
//#line 330 "Parser.y"
{
                      yyval.stmt = new Tree.WhileLoop(val_peek(2).expr, val_peek(0).stmt, val_peek(4).loc);
                    }
break;
case 62:
//#line 336 "Parser.y"
{
                      yyval.stmt = new Tree.If(val_peek(3).expr, val_peek(1).stmt, val_peek(0).stmt, val_peek(5).loc);
                    }
break;
case 63:
//#line 342 "Parser.y"
{
                      yyval.stmt = val_peek(0).stmt;
                    }
break;
case 64:
//#line 346 "Parser.y"
{
                      yyval = new SemValue();
                    }
break;
case 65:
//#line 352 "Parser.y"
{
                      yyval.stmt = new Tree.Switch(val_peek(4).expr, val_peek(1).cslist, val_peek(6).loc);
                    }
break;
case 66:
//#line 358 "Parser.y"
{
                      yyval.cslist.add(val_peek(0).csst);
                    }
break;
case 67:
//#line 362 "Parser.y"
{
                      yyval.cslist = new ArrayList<Tree.CaseStmt>();
                      yyval.cslist.add(val_peek(0).csst);
                    }
break;
case 68:
//#line 369 "Parser.y"
{
                      yyval.csst = new Tree.CaseStmt(val_peek(2).expr, val_peek(0).slist, val_peek(3).loc);
                    }
break;
case 69:
//#line 373 "Parser.y"
{
                      yyval.csst = new Tree.CaseStmt(null, val_peek(0).slist, val_peek(2).loc);
                    }
break;
case 70:
//#line 379 "Parser.y"
{
                      yyval.stmt = new Tree.Return(val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 71:
//#line 383 "Parser.y"
{
                      yyval.stmt = new Tree.Return(null, val_peek(0).loc);
                    }
break;
case 72:
//#line 389 "Parser.y"
{
                      yyval.stmt = new Tree.Break(val_peek(0).loc);
                    }
break;
case 73:
//#line 395 "Parser.y"
{
                      yyval.stmt = new Scanf(val_peek(1).elist, val_peek(3).loc);
                    }
break;
case 74:
//#line 401 "Parser.y"
{
                      yyval.stmt = new Printf(val_peek(1).elist, val_peek(3).loc);
                    }
break;
case 75:
//#line 407 "Parser.y"
{
                      yyval.elist.add(val_peek(0).lvalue);
                    }
break;
case 76:
//#line 411 "Parser.y"
{
                      yyval.elist = new ArrayList<Tree.Expr>();
                      yyval.elist.add(val_peek(0).lvalue);
                    }
break;
case 77:
//#line 418 "Parser.y"
{
                      yyval.lvalue = new Tree.Ident(val_peek(0).ident, val_peek(0).loc);
                    }
break;
case 78:
//#line 422 "Parser.y"
{
                      yyval.lvalue = new Tree.Indexed(val_peek(4).expr, val_peek(1).expr, val_peek(5).loc);
                    }
break;
case 79:
//#line 426 "Parser.y"
{
                      yyval.lvalue = new Tree.Indexed(val_peek(3).lvalue, val_peek(1).expr, val_peek(3).loc);
                    }
break;
case 80:
//#line 430 "Parser.y"
{
                      yyval.lvalue = new Tree.Indexed(val_peek(3).expr, val_peek(1).expr, val_peek(3).loc);
                    }
break;
case 81:
//#line 434 "Parser.y"
{
                      yyval.lvalue = new Tree.Indexed(val_peek(0).expr, null, val_peek(1).loc);
                    }
break;
case 82:
//#line 438 "Parser.y"
{
                      yyval = val_peek(1);
                    }
break;
case 87:
//#line 448 "Parser.y"
{
                      yyval.expr = val_peek(0).lvalue;
                    }
break;
case 88:
//#line 452 "Parser.y"
{
                      yyval.expr = new Tree.Unary(Tree.TAG.PRE_SA, val_peek(0).lvalue, val_peek(1).loc);
                    }
break;
case 89:
//#line 456 "Parser.y"
{
                      yyval.expr = new Tree.Unary(Tree.TAG.PRE_SS, val_peek(0).lvalue, val_peek(1).loc);
                    }
break;
case 90:
//#line 460 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.ASSIGN, val_peek(2).lvalue, val_peek(0).expr, val_peek(2).loc);
                    }
break;
case 91:
//#line 464 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.AE, val_peek(2).lvalue, val_peek(0).expr, val_peek(2).loc);
                    }
break;
case 92:
//#line 468 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.SE, val_peek(2).lvalue, val_peek(0).expr, val_peek(2).loc);
                    }
break;
case 93:
//#line 472 "Parser.y"
{
                      yyval.expr = new Tree.Unary(Tree.TAG.SUF_SA, val_peek(1).lvalue, val_peek(1).loc);
                    }
break;
case 94:
//#line 476 "Parser.y"
{
                      yyval.expr = new Tree.Unary(Tree.TAG.SUF_SS, val_peek(1).lvalue, val_peek(1).loc);
                    }
break;
case 95:
//#line 480 "Parser.y"
{
                      yyval = val_peek(1);
                    }
break;
case 96:
//#line 484 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.PLUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 97:
//#line 488 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.MINUS, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 98:
//#line 492 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.MUL, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 99:
//#line 496 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.DIV, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 100:
//#line 500 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.MOD, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 101:
//#line 504 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.LT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 102:
//#line 508 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.LE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 103:
//#line 512 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.GT, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 104:
//#line 516 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.GE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 105:
//#line 520 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.EQ, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 106:
//#line 524 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.NE, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 107:
//#line 528 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.AND, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 108:
//#line 532 "Parser.y"
{
                      yyval.expr = new Tree.Binary(Tree.TAG.OR, val_peek(2).expr, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 109:
//#line 536 "Parser.y"
{
                      yyval.expr = new Tree.Unary(Tree.TAG.NEG, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 110:
//#line 540 "Parser.y"
{
                      yyval.expr = new Tree.Unary(Tree.TAG.POS, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 111:
//#line 544 "Parser.y"
{
                      yyval.expr = new Tree.Unary(Tree.TAG.NOT, val_peek(0).expr, val_peek(1).loc);
                    }
break;
case 112:
//#line 548 "Parser.y"
{
                      yyval.expr = new Tree.TypeCast(val_peek(3).type, val_peek(2).marks.size(), val_peek(0).expr, val_peek(3).loc);
                    }
break;
case 113:
//#line 554 "Parser.y"
{
                      yyval.expr = new Tree.Literal(val_peek(0).typeTag, val_peek(0).literal, val_peek(0).loc);
                    }
break;
case 114:
//#line 558 "Parser.y"
{
                      yyval.expr = new Null(val_peek(0).loc);
                    }
break;
//#line 1485 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    //if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      //if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        //if (yychar<0) yychar=0;  //clean, if necessary
        //if (yydebug)
          //yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      //if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
//## The -Jnorun option was used ##
//## end of method run() ########################################



//## Constructors ###############################################
//## The -Jnoconstruct option was used ##
//###############################################################



}
//################### END OF CLASS ##############################
