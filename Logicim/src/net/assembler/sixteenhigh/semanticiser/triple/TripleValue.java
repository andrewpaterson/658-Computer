package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

public abstract class TripleValue
{
  public abstract PrimitiveTypeCode getTypeCode();

  public abstract String print();

  public TripleVariable getVariable()
  {
    return null;
  }
  public TripleLiteral getLiteral()
  {
    return null;
  }
}

