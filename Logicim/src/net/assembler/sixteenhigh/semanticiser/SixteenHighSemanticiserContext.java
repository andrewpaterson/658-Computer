package net.assembler.sixteenhigh.semanticiser;

import net.assembler.sixteenhigh.semanticiser.directive.DirectiveBlock;

public class SixteenHighSemanticiserContext
{
  protected String filename;
  protected DirectiveBlock currentDirectiveBlock;
  protected Routine currentRoutine;
  protected Struct currentStruct;
  protected Unit currentUnit;

  public SixteenHighSemanticiserContext(String filename)
  {
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

  public void setCurrentStruct(Struct struct)
  {
    currentStruct = struct;
  }

  public void setEnd()
  {
    currentRoutine = null;
    currentStruct = null;
  }
}

