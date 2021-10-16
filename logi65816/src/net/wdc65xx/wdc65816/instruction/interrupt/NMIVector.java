package net.wdc65xx.wdc65816.instruction.interrupt;

import net.wdc65xx.wdc65816.WDC65C816;

public class NMIVector
    extends InterruptVector
{
  @Override
  public int getAddress(WDC65C816 cpu)
  {
    if (cpu.isEmulation())
    {
      return 0xfffa;
    }
    else
    {
      return 0xffea;
    }
  }
}

