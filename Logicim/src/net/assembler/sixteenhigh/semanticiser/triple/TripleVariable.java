package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

public class TripleVariable
    extends TripleValue
{
  protected VariableDefinition variable;

  public TripleVariable(VariableDefinition variable)
  {
    this.variable = variable;
  }

  @Override
  public PrimitiveTypeCode getTypeCode()
  {
    return variable.getType().getType();
  }

  @Override
  public String print()
  {
    return variable.print();
  }

  public String getTypeName()
  {
    return variable.getName();
  }

  public String toString()
  {
    return variable.toString();
  }
}

