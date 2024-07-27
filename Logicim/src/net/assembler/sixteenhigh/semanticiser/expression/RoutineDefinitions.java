package net.assembler.sixteenhigh.semanticiser.expression;

import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.semanticiser.RoutineDefinition;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.assembler.sixteenhigh.common.scope.Scope.*;

public class RoutineDefinitions
{
  protected Map<String, RoutineDefinition> routines;
  protected Scope scope;
  protected String description;

  public RoutineDefinitions(Scope scope, String description)
  {
    this.routines = new LinkedHashMap<>();
    this.scope = scope;
    this.description = description;
  }

  public static RoutineDefinitions createGlobalRoutines()
  {
    return new RoutineDefinitions(global, GLOBAL);
  }

  public static RoutineDefinitions createUnitRoutines()
  {
    return new RoutineDefinitions(unit, UNIT);
  }

  public RoutineDefinition get(String name)
  {
    return routines.get(name);
  }

  public RoutineDefinition create(String name)
  {
    RoutineDefinition routine = get(name);
    if (routine != null)
    {
      throw new SimulatorException("%s Routine Definition [%s] already exists.", description, name);
    }
    routine = new RoutineDefinition(name, scope, this);
    routines.put(name, routine);
    return routine;
  }
}

