package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class ReadDataHigh
    extends DataOperation
{
  public ReadDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDataHigh(cpu.getPins().getData());
  }

  @Override
  public String toString()
  {
    return "Read(DH)";
  }
}

