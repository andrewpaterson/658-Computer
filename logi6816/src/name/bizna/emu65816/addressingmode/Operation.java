package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public abstract class CycleOperation
    implements BusCycleParameter
{
  public abstract void execute(Cpu65816 cpu);

  @Override
  public boolean isOperation()
  {
    return true;
  }

  @Override
  public boolean isAddress()
  {
    return false;
  }

  public boolean isData()
  {
    return false;
  }
}

