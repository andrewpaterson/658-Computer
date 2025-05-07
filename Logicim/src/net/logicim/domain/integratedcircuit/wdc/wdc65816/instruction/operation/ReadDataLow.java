package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ReadDataLow
    extends DataOperation
{
  public ReadDataLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().setDataLow(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(DL)";
  }
}

