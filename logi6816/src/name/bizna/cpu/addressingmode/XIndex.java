package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class XIndex
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getX();
  }

  @Override
  public String toString()
  {
    return "X";
  }
}
