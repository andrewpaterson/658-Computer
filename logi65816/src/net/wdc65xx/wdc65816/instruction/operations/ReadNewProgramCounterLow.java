package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class ReadNewProgramCounterLow
    extends DataOperation
{
  public ReadNewProgramCounterLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setNewProgramCounterLow(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(New_PCL)";
  }
}
