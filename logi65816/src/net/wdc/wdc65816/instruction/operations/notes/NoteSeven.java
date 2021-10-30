package net.wdc.wdc65816.instruction.operations.notes;

import net.wdc.wdc65816.W65C816;
import net.wdc.wdc65816.instruction.operations.Operation;

public class NoteSeven
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
  }

  @Override
  public boolean mustExecute(W65C816 cpu)
  {
    return !cpu.isEmulation();
  }

  @Override
  public String toString()
  {
    return "Note(7)";
  }
}

