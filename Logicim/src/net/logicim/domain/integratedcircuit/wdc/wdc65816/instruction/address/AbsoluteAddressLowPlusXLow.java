package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class AbsoluteAddressLowPlusXLow
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getState().getAddressOffsetX();
  }

  @Override
  public String toString()
  {
    return "(AAL+XL)";
  }
}

