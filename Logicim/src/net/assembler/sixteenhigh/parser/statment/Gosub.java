package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

public class Gosub
    extends Statement
{
  protected String label;

  public Gosub(Code code, int index, String label)
  {
    super(code, index);
    this.label = label;
  }
}

