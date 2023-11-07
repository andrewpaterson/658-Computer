package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public abstract class Statement
{
  protected Code code;
  protected int index;
  protected boolean semicolon;

  public Statement(Code code, int index)
  {
    this.code = code;
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
}

