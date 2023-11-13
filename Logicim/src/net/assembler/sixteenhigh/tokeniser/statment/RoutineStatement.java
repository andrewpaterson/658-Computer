package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;
import net.assembler.sixteenhigh.tokeniser.statment.scope.VariableScope;

public class RoutineStatement
    extends Statement
{
  protected String name;
  protected VariableScope scope;

  public RoutineStatement(Statements statements, int index, String name, VariableScope scope)
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

