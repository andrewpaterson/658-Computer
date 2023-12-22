package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.semanticiser.directive.DirectiveBlock;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;

import java.util.ArrayList;
import java.util.List;

public class SixteenHighSemanticiserContext
{
  protected String filename;
  protected DirectiveBlock currentDirectiveBlock;
  protected RoutineDefinition currentRoutine;
  protected StructDefinition currentStruct;
  protected UnitDefinition currentUnit;
  protected List<Block> blockStack;

  public SixteenHighSemanticiserContext(String filename)
  {
    this.filename = filename;
    this.currentDirectiveBlock = null;
    this.currentStruct = null;
    this.currentUnit = null;
    this.blockStack = new ArrayList<>();

  }

  public DirectiveBlock getCurrentDirectiveBlock()
  {
    return currentDirectiveBlock;
  }

  public void setCurrentDirectiveBlock(DirectiveBlock directiveBlock)
  {
    this.currentDirectiveBlock = directiveBlock;
  }

  public void setCurrentRoutine(RoutineDefinition routine)
  {
    currentRoutine = routine;
  }

  public void setCurrentStruct(StructDefinition struct)
  {
    currentStruct = struct;
  }

  public void setEnd()
  {
    currentRoutine = null;
    currentStruct = null;
  }

  public String getFilename()
  {
    return filename;
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

  public RoutineDefinition getRoutine(String name)
  {
    return null;
  }
}

