package net.assembler.sixteenhigh.parser.statment.directive;

import net.assembler.sixteenhigh.parser.Statements;
import net.assembler.sixteenhigh.parser.SixteenHighKeywords;
import net.assembler.sixteenhigh.parser.statment.Statement;

public class StartAddress
    extends Statement
{
  protected int address;

  public StartAddress(Statements statements, int index, int address)
  {
    super(statements, index);
    this.address = address;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$start_address 0x" + Integer.toHexString(address);
  }
}

