package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class ReadNewProgramCounterHigh
    extends DataOperation
{
  public ReadNewProgramCounterHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setNewProgramCounterHigh(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(New_PCH)";
  }
}

