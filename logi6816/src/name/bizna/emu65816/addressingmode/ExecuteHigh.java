package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class ExecuteHigh
    extends DataBusCycleOperation
{
  public ExecuteHigh(boolean read, boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, read, true);
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return cpu.isMemory8Bit() && cpu.isIndex8Bit();
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

