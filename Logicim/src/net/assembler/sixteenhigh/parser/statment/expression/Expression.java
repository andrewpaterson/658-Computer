package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class Expression
    extends BaseExpression
{
  public Expression()
  {
    super();
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder builder = new StringBuilder();
    if (expressions.size() > 1)
    {
      builder.append("(");
    }
    printExpressions(sixteenHighKeywords, builder);
    if (expressions.size() > 1)
    {
      builder.append(")");
    }
    return builder.toString();
  }

  @Override
  public boolean isExpression()
  {
    return true;
  }

}

