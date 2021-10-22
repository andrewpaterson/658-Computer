package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65C816;

public class ReadNewProgramCounterHigh
    extends DataOperation
{
  public ReadNewProgramCounterHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setNewProgramCounterHigh(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(New_PCH)";
  }
}

