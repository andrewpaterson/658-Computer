package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.interrupt;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class AbortVector
    extends InterruptVector
{
  @Override
  public int getAddress(W65C816 cpu)
  {
    if (cpu.getState().isEmulation())
    {
      return 0xfff8;
    }
    else
    {
      return 0xffe8;
    }
  }
}

