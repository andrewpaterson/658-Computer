package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

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

