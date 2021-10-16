package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class WriteProcessorStatus
    extends DataOperation
{
  public WriteProcessorStatus()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    setPinData(cpu, cpu.getProcessorRegisterValue());
  }

  @Override
  public String toString()
  {
    return "Write(P)";
  }
}

