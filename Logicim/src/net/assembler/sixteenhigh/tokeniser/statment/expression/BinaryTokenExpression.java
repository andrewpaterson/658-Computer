package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class BinaryTokenExpression
    implements TokenExpression
{
  protected TokenExpression leftExpression;
  protected SixteenHighKeywordCode operator;
  protected TokenExpression rightExpression;

  public BinaryTokenExpression(TokenExpression leftExpression,
                               SixteenHighKeywordCode operator,
                               TokenExpression rightExpression)
  {
    this.leftExpression = leftExpression;
    this.operator = operator;
    this.rightExpression = rightExpression;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "(" + leftExpression.print(sixteenHighKeywords) + " " + sixteenHighKeywords.getKeyword(operator) + " " + rightExpression.print(sixteenHighKeywords) + ")";
  }

  @Override
  public boolean isBinary()
  {
    return true;
  }

  public TokenExpression getLeftExpression()
  {
    return leftExpression;
  }

  public SixteenHighKeywordCode getOperator()
  {
    return operator;
  }

  public TokenExpression getRightExpression()
  {
    return rightExpression;
  }

  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }
}

