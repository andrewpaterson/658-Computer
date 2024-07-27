package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.interrupt;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class BRKVector
    extends InterruptVector
{
  @Override
  public int getAddress(W65C816 cpu)
  {
    if (cpu.isEmulation())
    {
      return 0xfffe;
    }
    else
    {
      return 0xffe6;
    }
  }
}

