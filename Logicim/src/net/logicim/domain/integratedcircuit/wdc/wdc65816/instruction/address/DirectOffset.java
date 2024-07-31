package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class DirectOffset
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getState().getDirectOffset();
  }

  @Override
  public String toString()
  {
    return "D0";
  }
}

