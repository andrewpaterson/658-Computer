package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.directive.Directive;
import net.assembler.sixteenhigh.semanticiser.expression.block.Block;
import net.assembler.sixteenhigh.semanticiser.types.RecordDefinition;
import net.common.SimulatorException;

public class SixteenHighSemanticiserContext
{
  protected Directive currentDirective;
  protected RoutineDefinition currentRoutine;
  protected RecordDefinition currentRecord;
  protected UnitDefinition unit;

  protected SixteenHighDefinition sixteenHighDefinition;

  public SixteenHighSemanticiserContext(UnitDefinition unit, SixteenHighDefinition sixteenHighDefinition)
  {
    this.unit = unit;
    this.sixteenHighDefinition = sixteenHighDefinition;
    this.currentDirective = null;
    this.currentRecord = null;
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

  public void setCurrentRecord(RecordDefinition struct)
  {
    currentRecord = struct;
  }

  public void setUnit(UnitDefinition unit)
  {
    this.unit = unit;
  }

  public void setEnd()
  {
    currentRoutine = null;
    currentRecord = null;
  }

  public RoutineDefinition getCurrentRoutine()
  {
    return currentRoutine;
  }

  public RecordDefinition getCurrentRecord()
  {
    return currentRecord;
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

