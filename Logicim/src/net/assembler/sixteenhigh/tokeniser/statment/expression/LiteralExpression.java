package net.assembler.sixteenhigh.tokeniser.statment.expression;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.tokeniser.literal.CTLiteral;

public class LiteralExpression
    implements Expressable
{
  public CTLiteral literal;

  public LiteralExpression(CTLiteral literal)
  {
    this.literal = literal;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return literal.print();
  }

  @Override
  public boolean isLiteral()
  {
    return true;
  }
}

