package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class WriteProcessorStatus
    extends DataOperation
{
  public WriteProcessorStatus()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().writeProcessorStatus();
  }

  @Override
  public String toString()
  {
    return "Write(P)";
  }
}

