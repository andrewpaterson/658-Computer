package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.common.scope.VariableScope;
import net.assembler.sixteenhigh.semanticiser.directive.Directive;
import net.assembler.sixteenhigh.semanticiser.expression.Variables;
import net.assembler.sixteenhigh.semanticiser.expression.block.Block;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;
import net.assembler.sixteenhigh.semanticiser.types.TypeDefinition;
import net.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

public class SixteenHighSemanticiserContext
{
  protected String filename;
  protected Directive currentDirective;
  protected RoutineDefinition currentRoutine;
  protected StructDefinition currentStruct;
  protected UnitDefinition currentUnit;

  protected Variables globalVariables;
  protected Map<String, RoutineDefinition> globalRoutines;

  public SixteenHighSemanticiserContext(String filename)
  {
    this.filename = filename;
    this.currentDirective = null;
    this.currentStruct = null;
    this.currentUnit = null;

    this.globalVariables = new Variables(VariableScope.global, "Global");
    this.globalRoutines = new LinkedHashMap<>();
  }

  public Directive getCurrentDirective()
  {
    return currentDirective;
  }

  public void setCurrentDirective(Directive directive)
  {
    this.currentDirective = directive;
  }

  public void setCurrentRoutine(RoutineDefinition routine)
  {
    currentRoutine = routine;
  }

  public void setCurrentStruct(StructDefinition struct)
  {
    currentStruct = struct;
  }

  public void setCurrentUnit(UnitDefinition unit)
  {
    currentUnit = unit;
  }

  public void setEnd()
  {
    currentRoutine = null;
    currentStruct = null;
  }

  public RoutineDefinition getCurrentRoutine()
  {
    return currentRoutine;
  }

  public StructDefinition getCurrentStruct()
  {
    return currentStruct;
  }

  public UnitDefinition getCurrentUnit()
  {
    return currentUnit;
  }

  public Block getCurrentBlock()
  {
    if (currentRoutine != null)
    {
      return currentRoutine.getBlock();
    }
    else if (currentUnit != null)
    {
      return currentUnit.getBlock();
    }
    else
    {
      throw new SimulatorException("Cannot get current block.");
    }
  }

  public VariableDefinition getVariable(String name)
  {
    if (currentRoutine != null)
    {
      VariableDefinition variable = currentRoutine.getVariable(name);
      if (variable != null)
      {
        return variable;
      }
    }

    if (currentUnit != null)
    {
      VariableDefinition variable = currentUnit.getVariable(name);
      if (variable != null)
      {
        return variable;
      }
    }

    return getGlobalVariable(name);
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
    return globalVariables.create(name, type);
  }
}

