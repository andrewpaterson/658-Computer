package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.interrupt;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ResetVector
    extends InterruptVector
{
  @Override
  public int getAddress(W65C816 cpu)
  {
    return 0xfffc;
  }
}

