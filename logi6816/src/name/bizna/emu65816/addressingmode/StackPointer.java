package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class StackPointer
    extends AddressOffset
{
  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return cpu.getStackPointer();
  }
}

