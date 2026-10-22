package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.semanticiser.expression.VariableDefinitions;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;

public class AutoVariableContext
{
  protected String baseName;
  protected VariableDefinitions variables;
  protected int index;

  public AutoVariableContext(String baseName, VariableDefinitions variables)
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
      name = baseName + "~" + index;
      VariableDefinition existing = variables.get(name);
      if (existing == null)
      {
        break;
      }
      index++;
    }
    return variables.create(name, typeDefinition, true);
  }
}

