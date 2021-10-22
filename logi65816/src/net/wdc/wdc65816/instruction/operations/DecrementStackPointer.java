package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class DecrementStackPointer
    extends Operation
{
  @Override
  public void execute(WDC65816 cpu)
  {
    cpu.decrementStackPointer();
  }

  @Override
  public String toString()
  {
    return "SP--";
  }
}

