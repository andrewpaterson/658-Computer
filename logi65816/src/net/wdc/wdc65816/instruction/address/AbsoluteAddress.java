package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;

public class AbsoluteAddress
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65816 cpu)
  {
    return cpu.getAddress().getOffset();
  }

  @Override
  public String toString()
  {
    return "AA";
  }
}

