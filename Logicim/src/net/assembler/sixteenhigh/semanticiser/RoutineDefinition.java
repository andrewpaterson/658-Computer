package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;

public class RoutineDefinition
{
  protected String name;
  protected VariableScope scope;

  public RoutineDefinition(String name, VariableScope scope)
  {
    this.name = name;
    this.scope = scope;
  }

  public boolean is(String name)
  {
    return this.name.equals(name);
  }

  public String getName()
  {
    return name;
  }

  public VariableScope getScope()
  {
    return scope;
  }
}

