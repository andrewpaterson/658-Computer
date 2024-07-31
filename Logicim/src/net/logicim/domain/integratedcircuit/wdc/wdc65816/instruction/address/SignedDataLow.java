package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class SignedDataLow
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getState().getData8BitOffset();
  }

  @Override
  public String toString()
  {
    return "RL";
  }
}

