package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public class GoTokenExpression
    extends FlowTokenExpression
{
  protected String label;

  public GoTokenExpression(String label)
  {
    this.label = label;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "go " + label;
  }
}

