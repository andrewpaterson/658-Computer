package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.directive.Directive;
import net.assembler.sixteenhigh.semanticiser.expression.block.Block;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;
import net.common.SimulatorException;

public class SixteenHighSemanticiserContext
{
  protected Directive currentDirective;
  protected RoutineDefinition currentRoutine;
  protected StructDefinition currentStruct;
  protected UnitDefinition unit;

  protected SixteenHighDefinition sixteenHighDefinition;

  public SixteenHighSemanticiserContext(UnitDefinition unit, SixteenHighDefinition sixteenHighDefinition)
  {
    this.unit = unit;
    this.sixteenHighDefinition = sixteenHighDefinition;
    this.currentDirective = null;
    this.currentStruct = null;
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

  public void setUnit(UnitDefinition unit)
  {
    this.unit = unit;
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

  public UnitDefinition getUnit()
  {
    return unit;
  }

  public Block getCurrentBlock()
  {
    if (currentRoutine != null)
    {
      return currentRoutine.getBlock();
    }
    else if (unit != null)
    {
      return unit.getBlock();
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

    if (unit != null)
    {
      VariableDefinition variable = unit.getVariable(name);
      if (variable != null)
      {
        return variable;
      }
    }

    return sixteenHighDefinition.getGlobalVariable(name);
  }
}

