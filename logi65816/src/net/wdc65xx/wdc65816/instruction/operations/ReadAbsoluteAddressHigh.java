package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;

public class ReadAbsoluteAddressHigh
    extends DataOperation
{
  public ReadAbsoluteAddressHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setAddressHigh(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(AAH)";
  }
}

