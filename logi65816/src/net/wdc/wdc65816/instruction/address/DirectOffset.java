package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65C816;

public class DirectOffset
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65C816 cpu)
  {
    return cpu.getDirectOffset();
  }

  @Override
  public String toString()
  {
    return "D0";
  }
}

