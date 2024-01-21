package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.tokeniser.literal.CTLiteral;

public class TripleLiteral
    extends TripleValue
{
  private CTLiteral literal;

  public TripleLiteral(CTLiteral literal)
  {

    this.literal = literal;
  }
}

