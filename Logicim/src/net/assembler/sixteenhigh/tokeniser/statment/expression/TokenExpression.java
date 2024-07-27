package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public interface TokenExpression
{
  String print(SixteenHighKeywords sixteenHighKeywords);

  default boolean isArrayInitialiser()
  {
    return false;
  }

  default boolean isList()
  {
    return false;
  }

  default boolean isPull()
  {
    return false;
  }

  default boolean isLiteral()
  {
    return false;
  }

  default boolean isOperand()
  {
    return false;
  }

  default boolean isBinary()
  {
    return false;
  }

  default boolean isUnary()
  {
    return false;
  }

  default boolean isVariable()
  {
    return false;
  }
}

