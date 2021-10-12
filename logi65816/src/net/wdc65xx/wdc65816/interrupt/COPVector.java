package net.wdc65xx.wdc65816.interrupt;

import net.wdc65xx.wdc65816.Cpu65816;

public class COPVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
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

