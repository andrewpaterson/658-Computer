package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class FetchNewProgramCounterBank
    extends DataBusCycleOperation
{
  public FetchNewProgramCounterBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setNewProgramCounterBank(cpu.getPinData());
  }
}

