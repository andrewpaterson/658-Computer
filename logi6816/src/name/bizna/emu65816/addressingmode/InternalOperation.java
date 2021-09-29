package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class InternalIgnored
    extends DataBusCycleOperation
{
  public InternalIgnored(boolean notMemoryLock)
  {
    super(false, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }
}

