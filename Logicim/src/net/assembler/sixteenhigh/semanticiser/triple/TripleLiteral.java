package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.tokeniser.literal.CTLiteral;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

public class TripleLiteral
    extends TripleValue
{
  private CTLiteral literal;

  public TripleLiteral(CTLiteral literal)
  {
    this.literal = literal;
  }

  @Override
  public PrimitiveTypeCode getTypeCode()
  {
    return literal.getPrimitiveTypeCode();
  }
}

