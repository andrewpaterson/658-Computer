package net.wdc.wdc65816.instruction.interrupt;

import net.wdc.wdc65816.WDC65816;

public class AbortVector
    extends InterruptVector
{
  @Override
  public int getAddress(WDC65816 cpu)
  {
    if (cpu.isEmulation())
    {
      return 0xfff8;
    }
    else
    {
      return 0xffe8;
    }
  }
}
