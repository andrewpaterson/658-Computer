package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class DirectOffset
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getDirectOffset();
  }

  @Override
  public String toString()
  {
    return "D0";
  }
}

