package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class InternalOperation
    extends DataOperation
{
  public InternalOperation(boolean notMemoryLock)
  {
    super(false, false, notMemoryLock, true, true);
  }

  public InternalOperation(boolean validProgramAddress, boolean validDataAddress, boolean notMemoryLock)
  {
    super(validProgramAddress, validDataAddress, notMemoryLock, true, true);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    getPinData(cpu);
  }

  @Override
  public String toString()
  {
    return "IO";
  }
}
