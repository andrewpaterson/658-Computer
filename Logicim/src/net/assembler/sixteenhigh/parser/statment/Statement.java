package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public abstract class Statement
{
  protected Statements statements;
  protected int index;
  protected boolean semicolon;

  public Statement(Statements statements, int index)
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
}

