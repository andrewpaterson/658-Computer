package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class ReadAbsoluteAddressLow
    extends DataOperation
{
  public ReadAbsoluteAddressLow(boolean notMemoryLock, boolean notVectorPull)
  {
    super(false, true, notMemoryLock, true, notVectorPull);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setAddressLow(cpu.getData());
  }

  @Override
  public String toString()
  {
    return "Read(AAL)";
  }
}

