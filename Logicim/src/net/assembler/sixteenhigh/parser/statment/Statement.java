package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

public abstract class Statement
{
  protected Code code;
  protected int index;

  public Statement(Code code, int index)
  {
    this.code = code;
    this.index = index;
  }
}

