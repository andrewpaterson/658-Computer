package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class ReadAbsoluteAddressLow
    extends DataOperation
{
  public ReadAbsoluteAddressLow(boolean notMemoryLock, boolean notVectorPull)
  {
    super(false, true, notMemoryLock, true, notVectorPull);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    cpu.setAddressLow(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(AAL)";
  }
}
