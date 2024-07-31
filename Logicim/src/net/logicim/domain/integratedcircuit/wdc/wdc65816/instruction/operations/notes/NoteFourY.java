package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.notes;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.Operation;

import static net.common.util.IntUtil.getLowByte;

public class NoteFourY
    extends Operation
{
  private final boolean nextWillRead;

  public NoteFourY(boolean nextWillRead)
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
    return cpu.getState().noteFourY(nextWillRead);
  }


  @Override
  public String toString()
  {
    return "Note(4)";
  }
}

