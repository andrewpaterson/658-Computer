package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.expression.UnitBlock;

import java.util.ArrayList;
import java.util.List;

public class UnitDefinition
{
  protected List<RoutineDefinition> routines;
  protected List<VariableDefinition> variables;
  protected UnitBlock block;

  public UnitDefinition()
  {
    routines = new ArrayList<>();
    variables = new ArrayList<>();
    block = new UnitBlock(this);
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

  public UnitBlock getBlock()
  {
    return block;
  }
}

