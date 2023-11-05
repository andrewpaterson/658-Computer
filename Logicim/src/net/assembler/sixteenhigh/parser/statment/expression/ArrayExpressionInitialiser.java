package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public class ArrayExpressionInitialiser
    extends BaseExpression
{
  public ArrayExpressionInitialiser()
  {
    super();
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    StringBuilder builder = new StringBuilder();
    builder.append("[");
    printCommaSeparatedExpressions(sixteenHighKeywords, builder);
    builder.append("]");
    return builder.toString();
  }
}

