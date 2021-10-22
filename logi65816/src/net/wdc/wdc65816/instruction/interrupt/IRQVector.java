package net.wdc.wdc65816.instruction.interrupt;

import net.wdc.wdc65816.WDC65C816;

public class IRQVector
    extends InterruptVector
{
  @Override
  public int getAddress(WDC65C816 cpu)
  {
    if (cpu.isEmulation())
    {
      return 0xfffe;
    }
    else
    {
      return 0xffee;
    }
  }
}

