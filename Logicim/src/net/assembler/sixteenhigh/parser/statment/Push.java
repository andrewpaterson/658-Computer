package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

public class Push
    extends Statement
{
  protected String register;

  public Push(Code code, int index, String register)
  {
    super(code, index);
    this.register = register;
  }
}

