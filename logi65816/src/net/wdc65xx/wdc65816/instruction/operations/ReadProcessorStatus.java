package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class ReadProcessorStatus
    extends DataOperation
{
  public ReadProcessorStatus()
  {
    super(false, true, true, true, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setProcessorRegisterValue(getPinData(cpu));
  }

  @Override
  public String toString()
  {
    return "Read(P)";
  }
}

