package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
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
  public void add(Expressable expressable)
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

