package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;

public class VariableDefinition
{
  protected String name;
  protected VariableScope scope;
  protected TypeDefinition type;

  public VariableDefinition(String name, VariableScope scope, TypeDefinition type)
  {
    this.name = name;
    this.scope = scope;
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public TypeDefinition getType()
  {
    return type;
  }

  public VariableScope getScope()
  {
    return scope;
  }
}

