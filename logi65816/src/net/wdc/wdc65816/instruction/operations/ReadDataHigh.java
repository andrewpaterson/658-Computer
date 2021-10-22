package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65C816;

public class ReadDataHigh
    extends DataOperation
{
  public ReadDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setDataHigh(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(DH)";
  }
}

