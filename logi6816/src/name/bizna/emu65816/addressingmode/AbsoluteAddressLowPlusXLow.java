package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Binary.getLowByte;
import static name.bizna.emu65816.Unsigned.toByte;

public class AAL_XL
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return toByte(getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getX()));
  }
}

