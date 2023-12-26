package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.expression.block.RoutineBlock;

public class RoutineDefinition
{
  protected String name;
  protected VariableScope scope;
  protected RoutineBlock block;

  public RoutineDefinition(String name, VariableScope scope)
  {
    this.name = name;
    this.scope = scope;
    this.block = new RoutineBlock(this);
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

  public RoutineBlock getBlock()
  {
    return block;
  }
}

