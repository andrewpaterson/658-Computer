package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class AbsoluteAddressHigh
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getAddress().getOffset() & 0xff00;
  }

  @Override
  public String toString()
  {
    return "AAH";
  }
}

