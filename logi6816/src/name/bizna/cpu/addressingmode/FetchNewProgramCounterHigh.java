package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

public class FetchNewProgramCounterHigh
    extends DataOperation
{
  public FetchNewProgramCounterHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setNewProgramCounterHigh(cpu.getPinData());
  }

  @Override
  public String toString()
  {
    return "Read(New_PCH)";
  }
}

