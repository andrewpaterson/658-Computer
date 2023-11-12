package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.statment.scope.VariableScope;

public class Routine
    extends Statement
{
  protected String name;
  protected VariableScope scope;

  public Routine(Statements statements, int index, String name, VariableScope scope)
  {
    super(statements, index);
    this.name = name;
    this.scope = scope;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return name + ":";
  }

  public boolean isRoutine()
  {
    return true;
  }
}

