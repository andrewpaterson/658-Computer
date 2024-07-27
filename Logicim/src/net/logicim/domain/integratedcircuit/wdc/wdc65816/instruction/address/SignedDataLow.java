package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class SignedDataLow
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    int dataLow = cpu.getDataLow();
    if (cpu.is8bitValueNegative(dataLow))
    {
      return dataLow | 0xffffff00;
    }
    else
    {
      return dataLow;
    }
  }

  @Override
  public String toString()
  {
    return "RL";
  }
}

