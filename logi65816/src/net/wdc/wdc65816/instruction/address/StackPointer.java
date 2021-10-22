package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;

public class StackPointer
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65816 cpu)
  {
    return cpu.getStackPointer();
  }

  @Override
  public String toString()
  {
    return "S";
  }
}

