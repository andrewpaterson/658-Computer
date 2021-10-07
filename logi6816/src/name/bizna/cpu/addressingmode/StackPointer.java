package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class StackPointer
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getStackPointer();
  }

  @Override
  public String toString()
  {
    return "S";
  }
}

