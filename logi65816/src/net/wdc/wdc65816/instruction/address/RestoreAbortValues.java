package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.instruction.operations.Operation;

public class RestoreAbortValues
    extends Operation
{
  @Override
  public void execute(WDC65816 cpu)
  {
    cpu.restoreAbortValues();
  }
}
