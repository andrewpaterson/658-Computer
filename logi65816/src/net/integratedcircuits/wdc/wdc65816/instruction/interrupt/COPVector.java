package net.integratedcircuits.wdc.wdc65816.instruction.interrupt;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class COPVector
    extends InterruptVector
{
  @Override
  public int getAddress(W65C816 cpu)
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

