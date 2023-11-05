package net.assembler.sixteenhigh.parser.statment.expression;

import net.assembler.sixteenhigh.parser.SixteenHighKeywordCode;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

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
    String keyword = sixteenHighKeywords.getKeyword(operator);
    return keyword + expressable.print(sixteenHighKeywords);
  }
}

