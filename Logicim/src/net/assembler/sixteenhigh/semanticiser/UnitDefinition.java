package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.expression.UnitBlock;
import net.assembler.sixteenhigh.semanticiser.expression.Variables;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

public class UnitDefinition
{
  protected Map<String, RoutineDefinition> routines;
  protected Variables variables;
  protected UnitBlock block;

  public UnitDefinition()
  {
    this.routines = new LinkedHashMap<>();
    this.variables = new Variables(VariableScope.unit, "Unit");
    this.block = new UnitBlock(this);
  }

  public RoutineDefinition getRoutine(String name)
  {
    return routines.get(name);
  }

  public VariableDefinition getVariable(String name)
  {
    return variables.get(name);
  }

  public RoutineDefinition createRoutine(String name)
  {
    RoutineDefinition routine = getRoutine(name);
    if (routine != null)
    {
      throw new SimulatorException("Unit Routine Definition [%s] already exists.", name);
    }
    routine = new RoutineDefinition(name, VariableScope.unit);
    routines.put(name, routine);
    return routine;
  }
  
  public VariableDefinition createVariable(String name, TypeDefinition type)
  {
    return variables.create(name, type);
  }

  public UnitBlock getBlock()
  {
    return block;
  }

  public Variables getVariables()
  {
    return variables;
  }
}

