package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;

public class DirectPage
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65816 cpu)
  {
    return cpu.getDirectPage();
  }

  @Override
  public String toString()
  {
    return "D";
  }
}

