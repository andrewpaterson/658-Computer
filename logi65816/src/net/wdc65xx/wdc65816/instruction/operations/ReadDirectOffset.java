package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class ReadDirectOffset
    extends DataOperation
{
  public ReadDirectOffset(boolean notMemoryLock)
  {
    super(true, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setDirectOffset(cpu.getPins().getData());
  }

  @Override
  public String toString()
  {
    return "Read(D0)";
  }
}

