package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;

public abstract class TokenStatement
{
  protected TokenUnit statements;
  protected int index;

  public TokenStatement(TokenUnit statements, int index)
  {
    this.statements = statements;
    this.index = index;
  }

  public abstract String print(SixteenHighKeywords sixteenHighKeywords);

  protected String semicolon()
  {
    return ";";
  }

  @Override
  public String toString()
  {
    return print(SixteenHighKeywords.getInstance());
  }

  public boolean isRoutine()
  {
    return false;
  }

  public boolean isRecord()
  {
    return false;
  }

  public boolean isEnd()
  {
    return false;
  }

  public boolean isLabel()
  {
    return false;
  }

  public boolean isDirective()
  {
    return false;
  }

  public int getIndex()
  {
    return index;
  }

  public boolean isVariableDefinition()
  {
    return false;
  }

  public boolean isAssignment()
  {
    return false;
  }
}

