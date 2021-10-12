package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

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
    cpu.setNewProgramCounterHigh(cpu.getPins().getData());
  }

  @Override
  public String toString()
  {
    return "Read(New_PCH)";
  }
}

