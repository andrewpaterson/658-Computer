package net.assembler.sixteenhigh.tokeniser.statment;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.TokenUnit;

public abstract class TokenStatement
{
  protected TokenUnit statements;
  protected int index;
  protected boolean semicolon;

  public TokenStatement(TokenUnit statements, int index)
  {
    this.statements = statements;
    this.index = index;
    this.semicolon = false;
  }

  public abstract String print(SixteenHighKeywords sixteenHighKeywords);

  public void appendSemicolon()
  {
    semicolon = true;
  }

  protected String semicolon()
  {
    if (semicolon)
    {
      return ";";
    }
    else
    {
      return "";
    }
  }

  public boolean isRoutine()
  {
    return false;
  }

  public boolean isStruct()
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

  public boolean isVariable()
  {
    return false;
  }
}

