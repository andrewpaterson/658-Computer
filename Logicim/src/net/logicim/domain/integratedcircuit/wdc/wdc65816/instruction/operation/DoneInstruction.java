package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class DoneInstruction
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
    cpu.getState().doneInstruction();
  }

  @Override
  public String toString()
  {
    return "DONE";
  }

  public int getDone8()
  {
    return 1;
  }

  public int getDone16()
  {
    return 1;
  }
}

