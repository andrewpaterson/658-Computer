package net.wdc.wdc65816.instruction.operations.notes;

import net.wdc.wdc65816.WDC65C816;
import net.wdc.wdc65816.instruction.operations.Operation;

public class NoteThree
    extends Operation
{
  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.createPartialAbortValues();
  }

  @Override
  public String toString()
  {
    return "Note(3)";
  }
}

