package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ReadAbsoluteAddressHigh
    extends DataOperation
{
  public ReadAbsoluteAddressHigh(boolean notMemoryLock, boolean notVectorPull)
  {
    super(false, true, notMemoryLock, true, notVectorPull);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setAddressHigh(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(AAH)";
  }
}

