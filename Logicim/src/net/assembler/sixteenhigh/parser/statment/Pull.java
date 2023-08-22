package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

public class Pull
    extends Statement
{
  protected String register;

  public Pull(Code code, int index, String register)
  {
    super(code, index);
    this.register = register;
  }
}

