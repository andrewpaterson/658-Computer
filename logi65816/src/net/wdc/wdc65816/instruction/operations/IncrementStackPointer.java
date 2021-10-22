package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;

public class IncrementStackPointer
    extends Operation
{
  @Override
  public void execute(WDC65816 cpu)
  {
    cpu.incrementStackPointer();
  }

  @Override
  public String toString()
  {
    return "SP++";
  }
}

