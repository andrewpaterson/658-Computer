package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.tokeniser.literal.CTLiteral;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

public class TripleLiteral
    extends TripleValue
{
  private CTLiteral ctLiteral;

  public TripleLiteral(CTLiteral literal)
  {
    this.ctLiteral = literal;
  }

  @Override
  public PrimitiveTypeCode getTypeCode()
  {
    return ctLiteral.getPrimitiveTypeCode();
  }

  @Override
  public String print()
  {
    return ctLiteral.print();
  }

  @Override
  public TripleLiteral getLiteral()
  {
    return this;
  }

  public CTLiteral getCTLiteral()
  {
    return ctLiteral;
  }
}

