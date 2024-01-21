package net.assembler.sixteenhigh.definition;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.RoutineDefinition;
import net.assembler.sixteenhigh.semanticiser.UnitDefinition;
import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.common.SimulatorException;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighDefinition
{
  protected List<RoutineDefinition> globalRoutines;
  protected List<VariableDefinition> globalVariables;
  protected List<UnitDefinition> units;

  public SixteenHighDefinition()
  {
    globalRoutines = new ArrayList<>();
    globalVariables = new ArrayList<>();
    units = new ArrayList<>();
  }

  public RoutineDefinition getGlobalRoutine(String routineName)
  {
    for (RoutineDefinition globalRoutine : globalRoutines)
    {
      if (globalRoutine.is(routineName))
      {
        return globalRoutine;
      }
    }

    return null;
  }

  public RoutineDefinition createUnitRoutine(UnitDefinition unit, String routineName, VariableScope routineScope)
  {
    if (routineScope != VariableScope.unit)
    {
      throw new SimulatorException("Expected Unit Routine scope to be [file].");
    }
    return unit.createRoutine(routineName);
  }

  public RoutineDefinition createGlobalRoutine(String routineName, VariableScope routineScope)
  {
    if (routineScope != VariableScope.global)
    {
      throw new SimulatorException("Expected Global Routine scope to be [global].");
    }

    RoutineDefinition routine = new RoutineDefinition(routineName, VariableScope.global);
    globalRoutines.add(routine);
    return routine;
  }
}

