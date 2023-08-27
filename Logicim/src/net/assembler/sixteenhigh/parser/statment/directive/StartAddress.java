package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.statment.Statement;

public class StartAddress
    extends Statement
{
  protected int address;

  public StartAddress(Code code, int index, int address)
  {
    super(code, index);
    this.address = address;
  }
}

