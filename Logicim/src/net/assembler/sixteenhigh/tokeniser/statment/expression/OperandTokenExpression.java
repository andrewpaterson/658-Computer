package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class OperandTokenExpression
    implements TokenExpression
{
  protected SixteenHighKeywordCode operand;

  public OperandTokenExpression(SixteenHighKeywordCode operand)
  {
    this.operand = operand;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return sixteenHighKeywords.getKeyword(operand);
  }

  public SixteenHighKeywordCode getOperand()
  {
    return operand;
  }

  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }

  @Override
  public boolean isOperand()
  {
    return true;
  }
}

