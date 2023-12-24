package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.SixteenHighKeywordCode;

public class OperandExpression
    implements Expression
{
  public SixteenHighKeywordCode operand;

  public OperandExpression(SixteenHighKeywordCode operand)
  {
    this.operand = operand;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return sixteenHighKeywords.getKeyword(operand);
  }
}

