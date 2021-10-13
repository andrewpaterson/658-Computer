package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class WriteDataHigh
    extends DataOperation
{
  public WriteDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getPins().setData(cpu.getDataHigh());
  }

  @Override
  public String toString()
  {
    return "Write(DH)";
  }
}

