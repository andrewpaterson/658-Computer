package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

import static net.logicim.common.util.IntUtil.getLowByte;
import static net.logicim.common.util.IntUtil.toByte;

public class AbsoluteAddressLowPlusYLow
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return toByte(getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getY()));
  }

  @Override
  public String toString()
  {
    return "(AAL+YL)";
  }
}

