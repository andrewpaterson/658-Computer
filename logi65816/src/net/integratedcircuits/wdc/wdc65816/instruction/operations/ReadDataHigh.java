package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;

public class ReadDataHigh
    extends DataOperation
{
  public ReadDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setDataHigh(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(DH)";
  }
}

