package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

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
    cpu.getState().setDataHigh(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(DH)";
  }
}

