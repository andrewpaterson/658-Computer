package net.wdc.wdc65816.instruction.operations.notes;

import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.instruction.operations.Operation;

public class NoteSeven
    extends Operation
{
  @Override
  public void execute(WDC65816 cpu)
  {
  }

  @Override
  public boolean mustExecute(WDC65816 cpu)
  {
    return !cpu.isEmulation();
  }

  @Override
  public String toString()
  {
    return "Note(7)";
  }
}
