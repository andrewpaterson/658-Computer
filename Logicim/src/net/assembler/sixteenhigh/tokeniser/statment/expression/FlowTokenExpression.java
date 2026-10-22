package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public abstract class FlowTokenExpression
    implements TokenExpression
{
  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }
}

