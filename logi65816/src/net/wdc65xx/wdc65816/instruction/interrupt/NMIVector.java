package net.wdc65xx.wdc65816.instruction.interrupt;

import net.wdc65xx.wdc65816.Cpu65816;

public class NMIVector
    extends InterruptVector
{
  @Override
  public int getAddress(Cpu65816 cpu)
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

