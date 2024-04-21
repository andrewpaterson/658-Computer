package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

public class TripleVariable
    extends TripleValue
{
  protected VariableDefinition type;

  public TripleVariable(VariableDefinition type)
  {
    this.type = type;
  }

  @Override
  public PrimitiveTypeCode getTypeCode()
  {
    return type.getType().getType();
  }

  public String getTypeName()
  {
    return type.getName();
  }
}

