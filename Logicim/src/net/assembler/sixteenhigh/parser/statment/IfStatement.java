package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.expression.FlowExpression;

public class IfStatement
    extends Statement
{
  protected SixteenHighKeywordCode type;
  protected FlowExpression go;

  public IfStatement(Code code, int index, SixteenHighKeywordCode type)
  {
    super(code, index);
    this.type = type;
  }

  public void setGo(FlowExpression go)
  {
    this.go = go;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return sixteenHighKeywords.getKeyword(type) + " " + go.print(sixteenHighKeywords) + semicolon();
  }
}

