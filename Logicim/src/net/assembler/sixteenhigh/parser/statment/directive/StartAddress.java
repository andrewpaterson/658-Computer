package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.parser.Code;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
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

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$start_address 0x" + Integer.toHexString(address);
  }
}

