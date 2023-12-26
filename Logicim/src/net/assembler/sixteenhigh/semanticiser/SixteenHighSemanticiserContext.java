package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.semanticiser.directive.Directive;
import net.assembler.sixteenhigh.semanticiser.expression.block.Block;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;
import net.common.SimulatorException;

public class SixteenHighSemanticiserContext
{
  protected String filename;
  protected Directive currentDirective;
  protected RoutineDefinition currentRoutine;
  protected StructDefinition currentStruct;
  protected UnitDefinition currentUnit;

  public SixteenHighSemanticiserContext(String filename)
  {
    this.filename = filename;
    this.currentDirective = null;
    this.currentStruct = null;
    this.currentUnit = null;
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
}

