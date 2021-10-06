package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class YIndex
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getY();
  }

  @Override
  public String toString()
  {
    return "Y";
  }
}

