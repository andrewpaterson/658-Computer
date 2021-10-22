package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;

import static net.util.IntUtil.getLowByte;
import static net.util.IntUtil.toByte;

public class AbsoluteAddressLowPlusXLow
    extends AddressOffset
{
  @Override
  public int getOffset(WDC65816 cpu)
  {
    return toByte(getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getX()));
  }

  @Override
  public String toString()
  {
    return "(AAL+XL)";
  }
}

