package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

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
    cpu.getState().readProcessorStatus();
  }

  @Override
  public String toString()
  {
    return "Read(P)";
  }
}

