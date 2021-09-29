package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class InternalOperation
    extends DataBusCycleOperation
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
  }
}

