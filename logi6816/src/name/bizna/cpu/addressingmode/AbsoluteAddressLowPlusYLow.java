package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

import static name.bizna.util.IntUtil.getLowByte;
import static name.bizna.util.IntUtil.toByte;

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

