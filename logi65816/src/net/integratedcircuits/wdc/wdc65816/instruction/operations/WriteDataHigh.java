package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class WriteDataHigh
    extends DataOperation
{
  public WriteDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.writePinData(cpu.getDataHigh());
  }

  @Override
  public String toString()
  {
    return "Write(DH)";
  }
}

