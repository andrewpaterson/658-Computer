package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.statment.Statement;

public class EndAddress
    extends Statement
{
  protected int address;

  public EndAddress(Code code, int index, int address)
  {
    super(code, index);
    this.address = address;
  }
}

