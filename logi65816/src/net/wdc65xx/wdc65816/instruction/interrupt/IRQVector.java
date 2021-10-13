package net.wdc65xx.wdc65816.instruction.interrupt;

import net.wdc65xx.wdc65816.Cpu65816;

public class IRQVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
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

