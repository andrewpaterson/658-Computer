package net.wdc.wdc65816.instruction.interrupt;

import net.wdc.wdc65816.W65C816;

public class NMIVector
    extends InterruptVector
{
  @Override
  public int getAddress(W65C816 cpu)
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

