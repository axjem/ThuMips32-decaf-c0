/*
 * 本文件是构造Decaf编译器所需要的JFlex输入脚本。
 * 在第一阶段，你需要完成这个脚本的内容，请参考"JFlex Manual"中关于如何编写JFlex脚本的说明。
 *
 * 注意：在UNIX系统下你需要保证这个文件使用UNIX文本格式，可使用dos2unix命令进行文本各式转换。
 */

package decaf.frontend;

import decaf.Location;
import decaf.error.*;
import decaf.utils.MiscUtils;
 
%%
%public
%class Lexer
%extends BaseLexer
%byaccj
%line
%column
%switch
%unicode
	
%{
	private Location sloc = null;
	private StringBuilder buffer = new StringBuilder();

	public Location getLocation() {
		return new decaf.Location(yyline + 1, yycolumn + 1);
	}
%}

NEWLINE				= (\r|\n|\r\n)
DIGIT 				= ([0-9])
HEX_DIGIT 			= ([0-9A-Fa-f])
HEX_INTEGER			= (0[Xx]{HEX_DIGIT}+)
DEC_INTEGER			= ({DIGIT}+)
INTEGER				= ({HEX_INTEGER}|{DEC_INTEGER})
FLOAT         = ({DEC_INTEGER}"."{DEC_INTEGER})
IDENTIFIER			= ([_0-9A-Za-z]*[_A-Za-z][_0-9A-Za-z]*)
SIMPLE_OPERATOR		= ("+"|"-"|"*"|"/"|"%"|"="|"<"|">"|"."|","|";"|"!"|"("|")"|"["|"]"|"{"|"}"|"&"|":")
S_COMMENT			= ("//"[^\r\n]*{NEWLINE})
WHITESPACE			= ([ \t]+)

	// 开始条件S表示字符串
%x S
  // 开始条件C表示字符
%x C
  // 开始条件D表示define字段
%x D
  // 开始条件DS, DC表示define内部的string和char
%x DS
%x DC

%%
  // 识别注释和空白字符的规则
{WHITESPACE}    { /* Just ignore */    }
{NEWLINE}     { /* Just ignore */    }
{S_COMMENT}     { /* Just ignore */    }

  // 识别关键字的规则  
"bool"        { return keyword(Parser.BOOL);      }
"break"       { return keyword(Parser.BREAK);     }
"case"        { return keyword(Parser.CASE);      }
"char"      { return keyword(Parser.CHAR);    }
"const"     { return keyword(Parser.CONST);   }
"default"   { return keyword(Parser.DEFAULT); }
"else"        { return keyword(Parser.ELSE);      }
"for"       { return keyword(Parser.FOR);     }
"float"     { return keyword(Parser.FLOAT);   }
"if"        { return keyword(Parser.IF);      }
"int"       { return keyword(Parser.INT);     }
"malloc"      { return keyword(Parser.MALLOC);    }
"NULL"        { return keyword(Parser.NULL);      }
"return"      { return keyword(Parser.RETURN);    }
"switch"      { return keyword(Parser.SWITCH);    }
"scanf"       { return keyword(Parser.SCANF);     }
"sizeof"      { return keyword(Parser.SIZEOF);    }
"void"        { return keyword(Parser.VOID);      }
"while"       { return keyword(Parser.WHILE);     }
"printf"       { return keyword(Parser.PRINTF);     }

  // 识别操作符的规则
"<="        { return operator(Parser.LESS_EQUAL); }
">="        { return operator(Parser.GREATER_EQUAL);}
"=="        { return operator(Parser.EQUAL);    }
"!="        { return operator(Parser.NOT_EQUAL);  }
"&&"        { return operator(Parser.AND);      }
"||"        { return operator(Parser.OR);     }
"++"        { return operator(Parser.SELF_ADD); }
"--"        { return operator(Parser.SELF_SUB); }
"+="        { return operator(Parser.ADD_EQUAL);  }
"-="        { return operator(Parser.SUB_EQUAL);  }
{SIMPLE_OPERATOR} { return operator((int)yycharat(0));  }

  // 识别常数的规则
"true"        { return boolConst(true);                   }
"false"       { return boolConst(false);                    }
{INTEGER}     { return intConst(yytext());      }
{FLOAT}       { return floatConst(yytext());    }

<YYINITIAL>\"		{ sloc = getLocation();		  
					  yybegin(S);
					  buffer = new StringBuilder();								    }
<S>\"				{ yybegin(YYINITIAL);
					  return stringConst(buffer.toString(), sloc);						}
<YYINITIAL>\'   { sloc = getLocation();
                yybegin(C);
                buffer = new StringBuilder();             }
<C>\'           { yybegin(YYINITIAL);
                  if (buffer.length() > 1) issueError(new MultiCharError(sloc, MiscUtils.quote(buffer.toString()))); 
                  return charConst(buffer.toString(), sloc);  }

  // 识别define
<YYINITIAL>"#"  { yybegin(D);
                  return operator((int)yycharat(0));  }
<D>{WHITESPACE}    { /* Just ignore */    }
<D>{S_COMMENT}     { /* Just ignore */    }
<D>"define"    { return keyword(Parser.DEFINE);    }
<D>{IDENTIFIER}   { return identifier(yytext());      }
<D>"true"       { return boolConst(true);                   }
<D>"false"       { return boolConst(false);                    }
<D>{INTEGER}     { return intConst(yytext());      }
<D>{FLOAT}       { return floatConst(yytext());    }
<D>\"           { sloc = getLocation();
                  yybegin(DS);
                  buffer = new StringBuilder();             }
<DS>\"       { yybegin(D);
            return stringConst(buffer.toString(), sloc);            }
<D>\'           { sloc = getLocation();
                yybegin(DC);
                buffer = new StringBuilder();             }
<DC>\'          { yybegin(D);
                  if (buffer.length() > 1) issueError(new MultiCharError(sloc, MiscUtils.quote(buffer.toString()))); 
                  else return charConst(buffer.toString(), sloc);  }
<D>{NEWLINE}    { yybegin(YYINITIAL);
                  return operator(Parser.NEW_LINE);   }
<S, C, DS, DC>{NEWLINE}   { issueError(new NewlineInStrError(sloc, MiscUtils.quote(buffer.toString()))); }
<S, DS><<EOF>>      { issueError(new UntermStrError(sloc, MiscUtils.quote(buffer.toString())));
            yybegin(YYINITIAL);                     }
<C, DC><<EOF>>  { issueError(new UntermCharError(sloc, MiscUtils.quote(buffer.toString())));
                  yybegin(YYINITIAL);     }
<S, C, DS, DC>"\\n"     { buffer.append('\n');                      }
<S, C, DS, DC>"\\t"     { buffer.append('\t');                      }
<S, C, DS, DC>"\\\""      { buffer.append('"');                     }
<S, C, DS, DC>"\\\'"      { buffer.append('\'');                     }
<S, C, DS, DC>"\\\\"      { buffer.append('\\');                      }
<S, C, DS, DC>"\\0"      { buffer.append('\0');                      }
<S, C, DS, DC>.        { buffer.append(yytext());                    }
<D>.         { issueError(new UnrecogCharError(getLocation(), yycharat(0)));     }

	// 识别标识符的规则
{IDENTIFIER}		{ return identifier(yytext());			}
	
	// 上面规则不能识别的字符怎么处理
.					{ issueError(new UnrecogCharError(getLocation(), yycharat(0))); 		}
