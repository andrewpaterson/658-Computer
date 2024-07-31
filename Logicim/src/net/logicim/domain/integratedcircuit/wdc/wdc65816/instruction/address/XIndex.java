package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class XIndex
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getState().getX();
  }

  @Override
  public String toString()
  {
    return "X";
  }
}

