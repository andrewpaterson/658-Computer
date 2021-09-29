package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public abstract class DataBusCycleOperation
    extends CycleOperation
{
  protected boolean validProgramAddress;
  protected boolean validDataAddress;
  protected boolean notMemoryLock;
  protected boolean read;
  protected boolean notVectorPull;

  public DataBusCycleOperation(boolean validProgramAddress, boolean validDataAddress, boolean notMemoryLock, boolean read, boolean notVectorPull)
  {
    this.validProgramAddress = validProgramAddress;
    this.validDataAddress = validDataAddress;
    this.notMemoryLock = notMemoryLock;
    this.read = read;
    this.notVectorPull = notVectorPull;
  }

  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return false;
  }

  public int executeWrite(Cpu65816 cpu)
  {
    return -1;
  }

  @Override
  public boolean isDataBus()
  {
    return true;
  }
}

