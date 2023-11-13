package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.common.SixteenHighKeywords;

public class OperandExpression
    implements Expressable
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

