package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;

public class Routine
{
  protected String name;
  protected VariableScope scope;

  public void setName(String name)
  {
    this.name = name;
  }

  public void setScope(VariableScope scope)
  {
    this.scope = scope;
  }
}

