package net.assembler.sixteenhigh.parser.statment;

import net.assembler.sixteenhigh.parser.Code;

public class Go
    extends Statement
{
  protected String label;

  public Go(Code code, int index, String label)
  {
    super(code, index);
    this.label = label;
  }
}
