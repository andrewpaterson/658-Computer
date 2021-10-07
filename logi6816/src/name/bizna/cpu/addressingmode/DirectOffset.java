package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class DirectOffset
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getDirectOffset();
  }

  @Override
  public String toString()
  {
    return "D0";
  }
}

