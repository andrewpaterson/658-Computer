package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.semanticiser.expression.RoutineDefinitions;
import net.assembler.sixteenhigh.semanticiser.expression.VariableDefinitions;
import net.assembler.sixteenhigh.semanticiser.expression.block.RoutineBlock;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;

public class RoutineDefinition
{
  protected String name;
  protected Scope scope;
  protected RoutineBlock block;

  protected VariableDefinitions variables;

  protected RoutineDefinitions parent;

  public RoutineDefinition(String name, Scope scope, RoutineDefinitions parent)
  {
    this.name = name;
    this.scope = scope;

    this.parent = parent;

    this.block = new RoutineBlock(this);
    this.variables = VariableDefinitions.createRoutineVariables();
  }

  public boolean is(String name)
  {
    return this.name.equals(name);
  }

  public String getName()
  {
    return name;
  }

  public Scope getScope()
  {
    return scope;
  }

  public RoutineBlock getBlock()
  {
    return block;
  }

  public VariableDefinition getVariable(String name)
  {
    return variables.get(name);
  }

  public VariableDefinition createVariable(String name, TypeDefinition type)
  {
    return variables.create(name, type, false);
  }

  public VariableDefinitions getVariables()
  {
    return variables;
  }
}

