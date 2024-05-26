package net.assembler.sixteenhigh.semanticiser.triple;

import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.assembler.sixteenhigh.tokeniser.literal.PrimitiveTypeCode;

public class TripleVariable
    extends TripleValue
{
  private VariableDefinition variableDefinition;

  public TripleVariable(VariableDefinition variable)
  {
    this.variableDefinition = variable;
  }

  @Override
  public PrimitiveTypeCode getTypeCode()
  {
    return variableDefinition.getType().getType();
  }

  @Override
  public String print()
  {
    return variableDefinition.print();
  }

  @Override
  public TripleVariable getVariable()
  {
    return this;
  }

  public VariableDefinition getVariableDefinition()
  {
    return variableDefinition;
  }

  public String getName()
  {
    return variableDefinition.getName();
  }

  public String toString()
  {
    return variableDefinition.toString();
  }
}

