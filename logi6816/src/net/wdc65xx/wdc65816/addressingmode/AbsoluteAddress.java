package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class AbsoluteAddress
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getAddress().getOffset();
  }

  @Override
  public String toString()
  {
    return "AA";
  }
}

