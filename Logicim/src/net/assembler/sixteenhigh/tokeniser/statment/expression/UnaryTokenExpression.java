package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class UnaryTokenExpression
    implements TokenExpression
{
  protected SixteenHighKeywordCode operator;
  protected TokenExpression expression;

  public UnaryTokenExpression(SixteenHighKeywordCode operator, TokenExpression expression)
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

  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }
}

