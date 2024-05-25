package net.assembler.sixteenhigh.definition;

import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.semanticiser.RoutineDefinition;
import net.assembler.sixteenhigh.semanticiser.UnitDefinition;
import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.assembler.sixteenhigh.semanticiser.expression.RoutineDefinitions;
import net.assembler.sixteenhigh.semanticiser.expression.VariableDefinitions;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;
import net.common.SimulatorException;

public class SixteenHighDefinition
{
  protected RoutineDefinitions globalRoutines;
  protected VariableDefinitions globalVariables;
  protected UnitDefinitions units;

  public SixteenHighDefinition()
  {
    globalRoutines = RoutineDefinitions.createGlobalRoutines();
    globalVariables = VariableDefinitions.createGlobalVariables();
    units = new UnitDefinitions();
  }

  public RoutineDefinition getGlobalRoutine(String routineName)
  {
    return globalRoutines.get(routineName);
  }

  public RoutineDefinition createUnitRoutine(UnitDefinition unit, String routineName, Scope routineScope)
  {
    if (routineScope != Scope.unit)
    {
      throw new SimulatorException("Expected Unit Routine scope to be [file].");
    }
    return unit.createRoutine(routineName);
  }

  public RoutineDefinition createGlobalRoutine(String routineName, Scope routineScope)
  {
    if (routineScope != Scope.global)
    {
      throw new SimulatorException("Expected Global Routine scope to be [global].");
    }

    return globalRoutines.create(routineName);
  }

  public VariableDefinition getGlobalVariable(String name)
  {
    VariableDefinition variable = globalVariables.get(name);
    if (variable != null)
    {
      return variable;
    }
    return null;
  }

  public VariableDefinition createGlobalVariable(String name, TypeDefinition type)
  {
    return globalVariables.create(name, type, false);
  }

  public UnitDefinition getUnit(String filename)
  {
    return units.get(filename);
  }

  public UnitDefinition createUnit(String filename)
  {
    return units.createUnit(filename);
  }
}

