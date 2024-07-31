package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

import static net.common.util.IntUtil.getLowByte;
import static net.common.util.IntUtil.toByte;

public class AbsoluteAddressLowPlusYLow
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return cpu.getState().getAddressOffsetY();
  }

  @Override
  public String toString()
  {
    return "(AAL+YL)";
  }
}

