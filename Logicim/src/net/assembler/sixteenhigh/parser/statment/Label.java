package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

public class Label
    extends Statement
{
  protected String name;

  public Label(Code code, int index, String name)
  {
    super(code, index);
    this.name = name;
  }
}

