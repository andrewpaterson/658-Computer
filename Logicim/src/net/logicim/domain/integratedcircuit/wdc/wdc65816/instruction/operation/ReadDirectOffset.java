package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ReadDirectOffset
    extends DataOperation
{
  public ReadDirectOffset(boolean notMemoryLock)
  {
    super(true, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().setDirectOffset(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(D0)";
  }
}

