package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public abstract class DataOperation
    extends Operation
{
  protected boolean validProgramAddress;
  protected boolean validDataAddress;
  protected boolean notMemoryLock;
  protected boolean read;
  protected boolean notVectorPull;

  public DataOperation(boolean validProgramAddress, boolean validDataAddress, boolean notMemoryLock, boolean read, boolean notVectorPull)
  {
    this.validProgramAddress = validProgramAddress;
    this.validDataAddress = validDataAddress;
    this.notMemoryLock = notMemoryLock;
    this.read = read;
    this.notVectorPull = notVectorPull;
  }

  @Override
  public boolean isData()
  {
    return true;
  }
}

