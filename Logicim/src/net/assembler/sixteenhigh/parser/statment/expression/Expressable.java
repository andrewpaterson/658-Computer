package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public interface Expressable
{
  String print(SixteenHighKeywords sixteenHighKeywords);

  default boolean isExpression()
  {
    return false;
  }
}

