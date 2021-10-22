package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class DecrementProgramCounter
    extends Operation
{
  @Override
  public void execute(WDC65816 cpu)
  {
    cpu.decrementProgramCounter();
  }

  @Override
  public String toString()
  {
    return "PC--";
  }
}

