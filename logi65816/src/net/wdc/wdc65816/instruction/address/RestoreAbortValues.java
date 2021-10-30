package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.W65C816;
import net.wdc.wdc65816.instruction.operations.Operation;

public class RestoreAbortValues
    extends Operation
{
  @Override
  public void execute(W65C816 cpu)
  {
    cpu.restoreAbortValues();
  }
}

