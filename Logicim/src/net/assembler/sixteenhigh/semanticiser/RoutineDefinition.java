package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.expression.Variables;
import net.assembler.sixteenhigh.semanticiser.expression.block.RoutineBlock;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;

public class RoutineDefinition
{
  protected String name;
  protected VariableScope scope;
  protected RoutineBlock block;

  protected Variables variables;

  public RoutineDefinition(String name, VariableScope scope)
  {
    this.name = name;
    this.scope = scope;
    this.block = new RoutineBlock(this);

    this.variables = new Variables(VariableScope.routine, "Routine");
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

  public VariableDefinition getVariable(String name)
  {
    return variables.get(name);
  }

  public VariableDefinition createVariable(String name, TypeDefinition type)
  {
    return variables.create(name, type);
  }

  public Variables getVariables()
  {
    return variables;
  }
}

