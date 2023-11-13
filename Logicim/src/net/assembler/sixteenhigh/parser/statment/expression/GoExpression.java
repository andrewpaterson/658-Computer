package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public class GoExpression
    extends FlowExpression
{
  protected String label;

  public GoExpression(String label)
  {
    this.label = label;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "go " + label;
  }
}

