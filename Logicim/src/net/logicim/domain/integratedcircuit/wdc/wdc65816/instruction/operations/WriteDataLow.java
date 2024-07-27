package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class WriteDataLow
    extends DataOperation
{
  public WriteDataLow(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setData(cpu.getDataLow());
  }

  @Override
  public String toString()
  {
    return "Write(DL)";
  }
}

