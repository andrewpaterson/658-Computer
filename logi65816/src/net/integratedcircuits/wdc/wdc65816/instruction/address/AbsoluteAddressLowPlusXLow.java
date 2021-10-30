package net.integratedcircuits.wdc.wdc65816.instruction.address;

import net.integratedcircuits.wdc.wdc65816.W65C816;

import static net.util.IntUtil.getLowByte;
import static net.util.IntUtil.toByte;

public class AbsoluteAddressLowPlusXLow
    extends AddressOffset
{
  @Override
  public int getOffset(W65C816 cpu)
  {
    return toByte(getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getX()));
  }

  @Override
  public String toString()
  {
    return "(AAL+XL)";
  }
}

