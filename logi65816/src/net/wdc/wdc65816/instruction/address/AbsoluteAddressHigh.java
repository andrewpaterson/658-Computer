package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;

public class AbsoluteAddressHigh
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65816 cpu)
  {
    return cpu.getAddress().getOffset() & 0xff00;
  }

  @Override
  public String toString()
  {
    return "AAH";
  }
}
