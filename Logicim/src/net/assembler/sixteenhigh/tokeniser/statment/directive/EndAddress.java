package net.assembler.sixteenhigh.tokeniser.statment.directive;

import net.assembler.sixteenhigh.common.SixteenHighKeywords;
import net.assembler.sixteenhigh.common.Statements;

public class EndAddress
    extends DirectiveStatement
{
  protected int address;

  public EndAddress(Statements statements, int index, int address)
  {
    super(statements, index);
    this.address = address;
  }

  @Override
  public String print(SixteenHighKeywords sixteenHighKeywords)
  {
    return "$end_address 0x" + Integer.toHexString(address);
  }

  public int getAddress()
  {
    return address;
  }
}
