package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class ExecuteLow
    extends DataBusCycleOperation
{
  public ExecuteLow(boolean read, boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, read, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public int executeWrite(Cpu65816 cpu)
  {
    return -1;
  }
}

