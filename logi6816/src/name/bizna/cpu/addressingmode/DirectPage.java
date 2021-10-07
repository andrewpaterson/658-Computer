package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class DirectPage
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getDirectPage();
  }

  @Override
  public String toString()
  {
    return "D";
  }
}

