package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public class GosubTokenExpression
    extends FlowTokenExpression
{
  protected String label;

  public GosubTokenExpression(String label)
  {
    this.label = label;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "gosub " + label;
  }
}

