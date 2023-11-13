package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public class GosubExpression
    extends FlowExpression
{
  protected String label;

  public GosubExpression(String label)
  {
    this.label = label;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "gosub " + label;
  }
}

