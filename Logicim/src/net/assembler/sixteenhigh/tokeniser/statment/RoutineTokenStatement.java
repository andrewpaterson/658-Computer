package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;
import net.assembler.sixteenhigh.common.scope.VariableScope;

public class RoutineTokenStatement
    extends TokenStatement
{
  protected String name;
  protected VariableScope scope;

  public RoutineTokenStatement(TokenUnit statements, int index, String name, VariableScope scope)
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

  public String getName()
  {
    return name;
  }

  public VariableScope getScope()
  {
    return scope;
  }
}

