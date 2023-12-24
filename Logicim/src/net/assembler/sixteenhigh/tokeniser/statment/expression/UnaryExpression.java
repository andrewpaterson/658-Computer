package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class UnaryExpression
    implements Expression
{
  public SixteenHighKeywordCode operator;
  public Expression expression;

  public UnaryExpression(SixteenHighKeywordCode operator, Expression expression)
  {
    this.operator = operator;
    this.expression = expression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    String leftBracket = "";
    String rightBracket = "";

    String keyword = sixteenHighKeywords.getKeyword(operator);
    if (operator == SixteenHighKeywordCode.not)
    {
      if (expression.isLiteral())
      {
        leftBracket = "(";
        rightBracket = ")";
      }
    }
    return keyword + leftBracket + expression.print(sixteenHighKeywords) + rightBracket;
  }
}

