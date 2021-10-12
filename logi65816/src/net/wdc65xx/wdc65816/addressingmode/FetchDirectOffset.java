package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;

public class FetchDirectOffset
    extends DataOperation
{
  public FetchDirectOffset(boolean notMemoryLock)
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

