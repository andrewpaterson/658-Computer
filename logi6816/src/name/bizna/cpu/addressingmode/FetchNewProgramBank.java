package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class FetchNewProgramBank
    extends DataOperation
{
  public FetchNewProgramBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setNewProgramCounterBank(cpu.getPins().getData());
  }

  @Override
  public String toString()
  {
    return "Read(New_PBR)";
  }
}

