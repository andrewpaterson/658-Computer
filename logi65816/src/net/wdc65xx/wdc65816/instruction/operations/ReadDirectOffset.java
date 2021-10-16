package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class ReadDirectOffset
    extends DataOperation
{
  public ReadDirectOffset(boolean notMemoryLock)
  {
    super(true, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setDirectOffset(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(D0)";
  }
}

