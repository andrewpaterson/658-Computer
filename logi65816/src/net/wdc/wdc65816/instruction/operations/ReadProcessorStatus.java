package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class ReadProcessorStatus
    extends DataOperation
{
  public ReadProcessorStatus()
  {
    super(false, true, true, true, true);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    cpu.setProcessorRegisterValue(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(P)";
  }
}
