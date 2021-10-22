package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class WriteDataHigh
    extends DataOperation
{
  public WriteDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, false, true);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    setPinData(cpu, cpu.getDataHigh());
  }

  @Override
  public String toString()
  {
    return "Write(DH)";
  }
}

