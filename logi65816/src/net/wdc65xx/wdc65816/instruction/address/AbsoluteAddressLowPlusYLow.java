package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Cpu65816;

import static net.util.IntUtil.getLowByte;
import static net.util.IntUtil.toByte;

public class AbsoluteAddressLowPlusYLow
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return toByte(getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getY()));
  }

  @Override
  public String toString()
  {
    return "(AAL+YL)";
  }
}

