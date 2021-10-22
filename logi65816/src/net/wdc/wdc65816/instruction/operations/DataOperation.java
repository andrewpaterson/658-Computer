package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public abstract class DataOperation
    extends Operation
{
  protected boolean validProgramAddress;
  protected boolean validDataAddress;
  protected boolean notMemoryLock;
  protected boolean read;
  protected boolean notVectorPull;
  protected boolean ready;

  public DataOperation(boolean validProgramAddress, boolean validDataAddress, boolean notMemoryLock, boolean read, boolean notVectorPull)
  {
    this.validProgramAddress = validProgramAddress;
    this.validDataAddress = validDataAddress;
    this.notMemoryLock = notMemoryLock;
    this.read = read;
    this.notVectorPull = notVectorPull;
    this.ready = true;
  }

  @Override
  public boolean isData()
  {
    return true;
  }

  public boolean isRead()
  {
    return read;
  }

  public boolean isValidProgramAddress()
  {
    return validProgramAddress;
  }

  public boolean isValidDataAddress()
  {
    return validDataAddress;
  }

  public boolean isNotMemoryLock()
  {
    return notMemoryLock;
  }

  public boolean isNotVectorPull()
  {
    return notVectorPull;
  }

  public boolean isReady()
  {
    return ready;
  }

  public boolean isFetchOpCode()
  {
    return false;
  }

  protected int getPinData(WDC65816 cpu)
  {
    return cpu.getPins().getData();
  }

  protected void setPinData(WDC65816 cpu, int data)
  {
    cpu.getPins().setData(data);
  }
}

