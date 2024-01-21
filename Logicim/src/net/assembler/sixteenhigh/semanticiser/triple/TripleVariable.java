package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

public class TripleVariable
    extends TripleValue
{
  protected PrimitiveTypeCode type;
  protected String name;

  public TripleVariable(PrimitiveTypeCode type, String name)
  {
    this.type = type;
    this.name = name;
  }
}

