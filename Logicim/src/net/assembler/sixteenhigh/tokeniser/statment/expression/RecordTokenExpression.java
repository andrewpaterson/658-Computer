package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public class RecordTokenExpression
    implements TokenExpression
{
  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return null;
  }

  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }
}

