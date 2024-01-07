package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.common.SimulatorException;

public class PullTokenExpression
    extends BaseTokenExpression
{
  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "<";
  }

  @Override
  public void add(TokenExpression expression)
  {
    throw new SimulatorException();
  }

  @Override
  public boolean isLiteral()
  {
    return false;
  }

  @Override
  public boolean isPull()
  {
    return false;
  }
}

