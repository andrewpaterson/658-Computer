package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.WDC65C816;

public class YIndex
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65C816 cpu)
  {
    return cpu.getY();
  }

  @Override
  public String toString()
  {
    return "Y";
  }
}

