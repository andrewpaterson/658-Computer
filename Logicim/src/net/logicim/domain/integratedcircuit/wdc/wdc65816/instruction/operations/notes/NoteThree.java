package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.notes;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations.Operation;

public class NoteThree
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
    cpu.createPartialAbortValues();
  }

  @Override
  public String toString()
  {
    return "Note(3)";
  }
}

