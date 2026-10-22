package net.assembler.sixteenhigh.semanticiser.expression;

import net.assembler.sixteenhigh.common.scope.Scope;
import net.assembler.sixteenhigh.semanticiser.VariableDefinition;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.assembler.sixteenhigh.common.scope.Scope.*;

public class VariableDefinitions
{
  protected Map<String, VariableDefinition> variables;
  protected Scope scope;
  protected String description;

  public VariableDefinitions(Scope scope, String description)
  {
    this.variables = new LinkedHashMap<>();
    this.scope = scope;
    this.description = description;
  }

  public static VariableDefinitions createGlobalVariables()
  {
    return new VariableDefinitions(global, GLOBAL);
  }

  public static VariableDefinitions createUnitVariables()
  {
    return new VariableDefinitions(unit, UNIT);
  }

  public static VariableDefinitions createRoutineVariables()
  {
    return new VariableDefinitions(routine, ROUTINE);
  }

  public VariableDefinition get(String name)
  {
    return variables.get(name);
  }

  public VariableDefinition create(String name, TypeDefinition type, boolean auto)
  {
    VariableDefinition variable = get(name);
    if (variable != null)
    {
      throw new SimulatorException("%s Variable Definition [%s] already exists.", description, name);
    }
    variable = new VariableDefinition(name, scope, type, this, auto);
    variables.put(name, variable);
    return variable;
  }

  public int numVariables(boolean includeAuto)
  {
    if (includeAuto)
    {
      return variables.size();
    }
    else
    {
      int count = 0;
      for (VariableDefinition value : variables.values())
      {
        if (!value.isAuto())
        {
          count++;
        }
      }
      return count;
    }
  }

  public int numAutoVariables()
  {
    int count = 0;
    for (VariableDefinition value : variables.values())
    {
      if (value.isAuto())
      {
        count++;
      }
    }
    return count;
  }
}

