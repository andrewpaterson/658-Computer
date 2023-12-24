package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.tokeniser.statment.expression.FlowExpression;

public class IfTokenStatement
    extends TokenStatement
{
  protected SixteenHighKeywordCode type;
  protected FlowExpression go;

  public IfTokenStatement(TokenUnit statements, int index, SixteenHighKeywordCode type)
  {
    super(statements, index);
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

