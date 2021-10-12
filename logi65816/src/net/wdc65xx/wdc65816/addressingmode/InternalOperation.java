package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

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
  public void execute(Cpu65816 cpu)
  {
    cpu.getPins().getData();
  }

  @Override
  public String toString()
  {
    return "IO";
  }
}

