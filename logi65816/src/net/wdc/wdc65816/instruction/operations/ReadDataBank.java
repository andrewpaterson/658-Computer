package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class ReadDataBank
    extends DataOperation
{
  public ReadDataBank(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    cpu.setDataBank(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(DBR)";
  }
}
