package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.notes;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.Address;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.Operation;

public class NoteSix
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public boolean mustExecute(W65C816 cpu)
  {
    return cpu.getState().noteSix();
  }

  @Override
  public String toString()
  {
    return "Note(6)";
  }
}

