package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.common.SimulatorException;

public class PullExpression
    extends BaseExpression
{
  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "<";
  }

  @Override
  public void add(Expression expression)
  {
    throw new SimulatorException();
  }

  @Override
  public boolean isLiteral()
  {
    return false;
  }

  @Override
  public boolean isAssignment()
  {
    return false;
  }
}

