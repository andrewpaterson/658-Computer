package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;

import java.util.ArrayList;
import java.util.List;

public class UnitDefinition
{
  protected List<RoutineDefinition> routines;
  protected List<VariableDefinition> variables;

  public UnitDefinition()
  {
    routines = new ArrayList<>();
    variables = new ArrayList<>();
  }

  public RoutineDefinition getRoutine(String routineName)
  {
    for (RoutineDefinition globalRoutine : routines)
    {
      if (globalRoutine.is(routineName))
      {
        return globalRoutine;
      }
    }

    return null;
  }

  public RoutineDefinition createUnitRoutine(String routineName)
  {
    RoutineDefinition routine = new RoutineDefinition(routineName, VariableScope.file);
    routines.add(routine);
    return routine;
  }
}

