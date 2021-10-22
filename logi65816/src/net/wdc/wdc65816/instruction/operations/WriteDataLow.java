package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65C816;

public class WriteDataLow
    extends DataOperation
{
  public WriteDataLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, false, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    setPinData(cpu, cpu.getDataLow());
  }

  @Override
  public String toString()
  {
    return "Write(DL)";
  }
}

