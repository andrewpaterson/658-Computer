package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class IncrementStackPointer
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().incrementStackPointer();
  }

  @Override
  public String toString()
  {
    return "SP++";
  }
}

