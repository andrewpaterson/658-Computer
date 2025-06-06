package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class WriteProgramCounterHigh
    extends DataOperation
{
  public WriteProgramCounterHigh()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().writeProgramCounterHigh();
  }

  @Override
  public String toString()
  {
    return "Write(PCH)";
  }
}

