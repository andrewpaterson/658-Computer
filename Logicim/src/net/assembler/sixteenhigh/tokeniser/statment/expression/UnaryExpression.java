package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class UnaryExpression
    implements Expressable
{
  public SixteenHighKeywordCode operator;
  public Expressable expressable;

  public UnaryExpression(SixteenHighKeywordCode operator, Expressable expressable)
  {
    this.operator = operator;
    this.expressable = expressable;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    String leftBracket = "";
    String rightBracket = "";

    String keyword = sixteenHighKeywords.getKeyword(operator);
    if (operator == SixteenHighKeywordCode.not)
    {
      if (expressable.isLiteral())
      {
        leftBracket = "(";
        rightBracket = ")";
      }
    }
    return keyword + leftBracket + expressable.print(sixteenHighKeywords) + rightBracket;
  }
}

