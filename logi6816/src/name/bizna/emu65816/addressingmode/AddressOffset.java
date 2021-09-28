package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public abstract class AddressOffset
    implements BusCycleParameter
{
  @Override
  public boolean isOperation()
  {
    return false;
  }

  @Override
  public boolean isAddressOffset()
  {
    return true;
  }

  public int getBank(Cpu65816 cpu)
  {
    return 0;
  }

  public abstract int getOffset(Cpu65816 cpu);
}

