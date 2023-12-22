package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;

public class RoutineDefinition
{
  protected String name;
  protected VariableScope scope;
  protected Block block;

  public RoutineDefinition(String name, VariableScope scope, Block block)
  {
    this.name = name;
    this.scope = scope;
    this.block = block
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

  public Block getBlock()
  {
    return block;
  }
}

