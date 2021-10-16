package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.WDC65C816;

public class IncrementStackPointer
    extends Operation
{
  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.incrementStackPointer();
  }

  @Override
  public String toString()
  {
    return "SP++";
  }
}

