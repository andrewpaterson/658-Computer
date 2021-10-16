package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Address;
import net.wdc65xx.wdc65816.WDC65C816;

public class DataBankAndAbsoluteAddress
    extends OffsetAddressCycle
{
  public DataBankAndAbsoluteAddress(int offset)
  {
    super(offset);
  }

  @Override
  public Address getAddress(WDC65C816 cpu)
  {
    return new Address(cpu.getDataBank(), cpu.getAddress().getOffset()).offset(offset, false);
  }
}

