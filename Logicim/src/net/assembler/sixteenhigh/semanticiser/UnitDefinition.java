package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.semanticiser.expression.RoutineDefinitions;
import net.assembler.sixteenhigh.semanticiser.expression.UnitBlock;
import net.assembler.sixteenhigh.semanticiser.expression.VariableDefinitions;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;
import net.common.SimulatorException;

public class UnitDefinition
{
  protected RoutineDefinitions routines;
  protected VariableDefinitions variables;
  protected UnitBlock block;
  protected String filename;

  public UnitDefinition(String filename)
  {
    this.routines = RoutineDefinitions.createUnitRoutines();
    this.variables = VariableDefinitions.createUnitVariables();
    this.block = new UnitBlock(this);
    this.filename = filename;
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
    return routines.create(name);
  }

  public VariableDefinition createVariable(String name, TypeDefinition type)
  {
    return variables.create(name, type, false);
  }

  public UnitBlock getBlock()
  {
    return block;
  }

  public VariableDefinitions getVariables()
  {
    return variables;
  }
}

