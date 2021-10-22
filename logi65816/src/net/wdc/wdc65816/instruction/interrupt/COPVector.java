package net.wdc.wdc65816.instruction.interrupt;

import net.wdc.wdc65816.WDC65C816;

public class COPVector
    extends InterruptVector
{
  @Override
  public int getAddress(WDC65C816 cpu)
  {
    if (cpu.isEmulation())
    {
      return 0xfff4;
    }
    else
    {
      return 0xffe4;
    }
  }
}

