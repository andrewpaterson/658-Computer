package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;

public abstract class Statement
{
  protected Code code;
  protected int index;

  public Statement(Code code, int index)
  {
    this.code = code;
    this.index = index;
  }

  public abstract String print(SixteenHighKeywords sixteenHighKeywords);
}

