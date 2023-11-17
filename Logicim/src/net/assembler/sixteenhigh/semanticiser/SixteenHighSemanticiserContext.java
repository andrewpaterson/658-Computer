package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.definition.SixteenHighDefinition;
import net.assembler.sixteenhigh.semanticiser.directive.DirectiveBlock;
import net.assembler.sixteenhigh.semanticiser.types.StructDefinition;

public class SixteenHighSemanticiserContext
{
  protected String filename;
  protected DirectiveBlock currentDirectiveBlock;
  protected Routine currentRoutine;
  protected StructDefinition currentStruct;
  protected UnitDefinition currentUnit;
  protected SixteenHighDefinition sixteenHighDefinition;

  public SixteenHighSemanticiserContext(SixteenHighDefinition sixteenHighDefinition, String filename)
  {
    this.sixteenHighDefinition = sixteenHighDefinition;
    this.filename = filename;
    this.currentDirectiveBlock = null;
    this.currentStruct = null;
    this.currentUnit = null;
  }

  public DirectiveBlock getCurrentDirectiveBlock()
  {
    return currentDirectiveBlock;
  }

  public void setCurrentDirectiveBlock(DirectiveBlock directiveBlock)
  {
    this.currentDirectiveBlock = directiveBlock;
  }

  public void setCurrentRoutine(Routine routine)
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

  public Routine getCurrentRoutine()
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

  public Routine getRoutine(String name)
  {
    return null;
  }
}

