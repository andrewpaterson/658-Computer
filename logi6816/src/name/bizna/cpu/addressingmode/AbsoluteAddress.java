package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class AbsoluteAddress
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getAddress().getOffset();
  }

  @Override
  public String toString()
  {
    return "AA";
  }
}

