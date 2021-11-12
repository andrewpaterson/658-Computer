package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class ReadProcessorStatus
    extends DataOperation
{
  public ReadProcessorStatus()
  {
    super(false, true, true, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setProcessorRegisterValue(cpu.readPinData());
  }

  @Override
  public String toString()
  {
    return "Read(P)";
  }
}

