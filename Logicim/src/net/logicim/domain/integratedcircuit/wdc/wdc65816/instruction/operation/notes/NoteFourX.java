package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation.notes;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation.Operation;

public class NoteFourX
    extends Operation
{
  private boolean nextWillRead;

  public NoteFourX(boolean nextWillRead)
  {
    this.nextWillRead = nextWillRead;
  }

  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public boolean mustExecute(W65C816 cpu)
  {
    return cpu.getState().noteFourX(nextWillRead);
  }

  @Override
  public String toString()
  {
    return "Note(4)";
  }
}

