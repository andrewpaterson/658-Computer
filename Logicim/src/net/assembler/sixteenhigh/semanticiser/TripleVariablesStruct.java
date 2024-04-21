package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.semanticiser.expression.Variables;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;

public class TripleVariablesStruct
{
  protected String baseName;
  protected Variables variables;
  protected int index;

  public TripleVariablesStruct(String baseName, Variables variables)
  {
    this.baseName = baseName;
    this.variables = variables;
    this.index = 0;
  }

  public VariableDefinition create(TypeDefinition typeDefinition)
  {
    String name;
    for (; ; )
    {
      name = baseName + "_" + index;
      VariableDefinition existing = variables.get(name);
      if (existing == null)
      {
        break;
      }
    }
    VariableDefinition variable = variables.create(name, typeDefinition);
    index++;
    return variable;
  }
}

