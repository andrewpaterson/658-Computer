package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public interface Expression
{
  String print(SixteenHighKeywords sixteenHighKeywords);

  default boolean isExpression()
  {
    return false;
  }

  default boolean isArrayExpressionInitialiser()
  {
    return false;
  }

  default boolean isLiteral()
  {
    return false;
  }
}

